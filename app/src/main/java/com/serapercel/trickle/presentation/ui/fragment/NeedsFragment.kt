package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.serapercel.trickle.R
import com.serapercel.trickle.common.adapter.NeedsAdapter
import com.serapercel.trickle.databinding.FragmentNeedsBinding
import com.serapercel.trickle.presentation.ui.viewModel.AccountViewModel
import com.serapercel.trickle.presentation.ui.viewModel.NeedsViewModel
import com.serapercel.trickle.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NeedsFragment : Fragment() {

    private var _binding: FragmentNeedsBinding?= null
    private val binding get() = _binding!!

    private lateinit var needsViewModel :NeedsViewModel
    private val needsAdapter by lazy { NeedsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNeedsBinding.inflate(inflater, container,false)

        needsViewModel = ViewModelProvider(this).get(NeedsViewModel::class.java)

        setupRecyclerView()
        readDatabase()

        return binding.root
    }

    private fun setupRecyclerView(){
        with(binding){
            rvNeeds.adapter = needsAdapter
            rvNeeds.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun readDatabase(){
        lifecycleScope.launch {
            needsViewModel.reedNeeds.observeOnce(viewLifecycleOwner){database ->
                if (database.isNotEmpty()){
                    needsAdapter.setData(database)
                }
            }
        }
    }

}