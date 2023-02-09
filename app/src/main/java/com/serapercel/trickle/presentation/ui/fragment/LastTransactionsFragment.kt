package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.serapercel.trickle.common.adapter.LastTransactionAdapter
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.databinding.FragmentLastTransactionsBinding
import com.serapercel.trickle.presentation.ui.viewModel.LastTransactionsViewModel
import com.serapercel.trickle.util.NetworkListener
import com.serapercel.trickle.util.NetworkResult
import com.serapercel.trickle.util.toastShort
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LastTransactionsFragment @Inject constructor(
    var user: User
) : Fragment() {

    private var _binding: FragmentLastTransactionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var transactionsViewModel: LastTransactionsViewModel
    private val transactionsAdapter by lazy { LastTransactionAdapter() }

    private lateinit var networkListener: NetworkListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLastTransactionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionsViewModel = ViewModelProvider(this).get(LastTransactionsViewModel::class.java)

        setupRecyclerView()

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    transactionsViewModel.networkStatus = status
                    transactionsViewModel.showNetworkStatus()
                    if (status) {
                        requestFirebaseData()
                    } else {
                        requireContext().toastShort("No Internet Connection!")
                    }
                }
        }
    }

    private fun requestFirebaseData() {
        transactionsViewModel.getTransactions(user)
        transactionsViewModel.transactionResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        transactionsAdapter.setData(newData = it)
                    }
                }
                is NetworkResult.Error -> {
                    requireContext().toastShort(response.message.toString())
                }
                is NetworkResult.Loading -> {
                    requireContext().toastShort("Loading")
                }
            }
        }
    }

    private fun setupRecyclerView() {
        with(binding) {
            rvLastTransactions.adapter = transactionsAdapter
            rvLastTransactions.layoutManager = LinearLayoutManager(requireContext())
        }
    }

}