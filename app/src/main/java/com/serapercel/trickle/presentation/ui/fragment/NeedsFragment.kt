package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.serapercel.trickle.common.adapter.NeedsAdapter
import com.serapercel.trickle.databinding.FragmentNeedsBinding
import com.serapercel.trickle.presentation.ui.viewModel.NeedsViewModel
import com.serapercel.trickle.util.NetworkListener
import com.serapercel.trickle.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NeedsFragment : Fragment() {

    private var _binding: FragmentNeedsBinding? = null
    private val binding get() = _binding!!

    private lateinit var needsViewModel: NeedsViewModel
    private val needsAdapter by lazy { NeedsAdapter() }

    private lateinit var networkListener: NetworkListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNeedsBinding.inflate(inflater, container, false)

        needsViewModel = ViewModelProvider(this).get(NeedsViewModel::class.java)

        setupRecyclerView()
        observeBackOnline()

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailabiliy(requireContext())
                .collect { status ->
                    needsViewModel.networkStatus = status
                    needsViewModel.showNetworkStatus()
                    readDatabase()
                }
        }

        return binding.root
    }

    private fun observeBackOnline() {
        needsViewModel.readBackOnline.observe(viewLifecycleOwner) {
            needsViewModel.backOnline = it
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            rvNeeds.adapter = needsAdapter
            rvNeeds.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            needsViewModel.reedNeeds.observeOnce(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    needsAdapter.setData(database)
                } else {
                    requestFirebaseData()
                }
            }
        }
    }

    private fun requestFirebaseData() {

    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            needsViewModel.reedNeeds.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    needsAdapter.setData(database)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}