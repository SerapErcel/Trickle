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
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.databinding.FragmentNeedsBinding
import com.serapercel.trickle.presentation.ui.viewModel.NeedsViewModel
import com.serapercel.trickle.util.NetworkListener
import com.serapercel.trickle.util.NetworkResult
import com.serapercel.trickle.util.observeOnce
import com.serapercel.trickle.util.toastShort
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NeedsFragment @Inject constructor(
    var user: User
) : Fragment() {

    private var _binding: FragmentNeedsBinding? = null
    private val binding get() = _binding!!

    private lateinit var needsViewModel: NeedsViewModel
    private val needsAdapter by lazy { NeedsAdapter(needsViewModel, requireActivity(), user) }

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
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    needsViewModel.networkStatus = status
                    needsViewModel.showNetworkStatus()
                    if (status) {
                        requestFirebaseData()
                    } else {
                        readDatabase()

                    }
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
        needsViewModel.getNeeds(user)
        needsViewModel.needsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        needsAdapter.setData(newData = it)
                    }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    requireContext().toastShort(response.message.toString())
                }
                is NetworkResult.Loading -> {
                    requireContext().toastShort("Loading")
                }
            }
        }
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