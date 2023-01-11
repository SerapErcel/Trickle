package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.serapercel.trickle.databinding.FragmentHomeBinding
import com.serapercel.trickle.presentation.ui.viewModel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private var accountUuid = 0
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        arguments?.let {
            viewModel.accountLiveData.value=HomeFragmentArgs.fromBundle(it).account
        }
        observeLiveData()

    }

    private fun observeLiveData(){
        viewModel.getDataFromSharedPref(requireContext())
        viewModel.accountLiveData.observe(viewLifecycleOwner, Observer { account ->
            account?.let {
                binding.tvToolbarAccountName.text = account.name
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}