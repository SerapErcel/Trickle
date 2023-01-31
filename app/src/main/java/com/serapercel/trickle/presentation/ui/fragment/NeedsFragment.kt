package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
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

@AndroidEntryPoint
class NeedsFragment : Fragment() {

    private var _binding: FragmentNeedsBinding? = null
    private val binding get() = _binding!!

    private lateinit var needsViewModel: NeedsViewModel
    private val needsAdapter by lazy { NeedsAdapter() }

    private lateinit var networkListener: NetworkListener
    lateinit var user: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNeedsBinding.inflate(inflater, container, false)

        user = User("1", "serap@gmail.com")
        needsViewModel = ViewModelProvider(this).get(NeedsViewModel::class.java)

        setupRecyclerView()
        observeBackOnline()

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    needsViewModel.networkStatus = status
                    needsViewModel.showNetworkStatus()
                    if(status){
                        requestFirebaseData()
                        Log.e("hata", "status = $status")
                    }else{
                        readDatabase()
                        Log.e("hata", "status = $status")

                    }
                }
        }

        return binding.root
    }

    private fun observeBackOnline() {
        needsViewModel.readBackOnline.observe(viewLifecycleOwner) {
            needsViewModel.backOnline = it
            Log.e("hata", "observeBackOnline tetiklendi - backOnline ${it}")

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
                    Log.e("hata", "database = $database")
                    needsAdapter.setData(database)
                } else {
                    Log.e("hata", "database is empty")
                    requestFirebaseData()
                }

            }
        }
    }

    private fun requestFirebaseData() {
        Log.e("hata", "request firebase data")
        needsViewModel.getNeeds(user)
        needsViewModel.needsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        Log.e("hata", "request firebase data ${response.data}")
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
        Log.e("hata", "loadDataFromCache tetiklendi ")
        lifecycleScope.launch {
            needsViewModel.reedNeeds.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    Log.e("hata", "loadDataFromCache")
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