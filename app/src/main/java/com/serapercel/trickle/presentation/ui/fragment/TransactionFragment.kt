package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.serapercel.trickle.common.adapter.TransactionAdapter
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.databinding.FragmentTransactionBinding
import com.serapercel.trickle.presentation.ui.viewModel.TransactionsViewModel
import com.serapercel.trickle.util.NetworkListener
import com.serapercel.trickle.util.NetworkResult
import com.serapercel.trickle.util.toastShort
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class TransactionFragment @Inject constructor(
    var account: Account
) : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    private lateinit var transactionsViewModel: TransactionsViewModel
    private val transactionsAdapter by lazy {
        TransactionAdapter(
            requireContext(),
            transactionsViewModel,
            account
        )
    }

    private lateinit var networkListener: NetworkListener
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionsViewModel = ViewModelProvider(this)[TransactionsViewModel::class.java]

        setupRecyclerView()
        binding.tvTotal.text = transactionsAdapter.getTotal().toString()

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

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

    }

    fun filterList(query: String?) {
        if (query != null) {
            val filteredList = ArrayList<ITransaction>()
            transactionsViewModel.transactionResponse.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Success -> {
                        response.data?.let {
                            for (i in it) {
                                if (i.title.lowercase(Locale.ROOT).contains(query) ||
                                    i.account.name.lowercase(Locale.ROOT).contains(query)
                                ) {
                                    filteredList.add(i)
                                }
                            }
                            if (filteredList.isEmpty()) {
                                requireContext().toastShort("No Data Found")
                            } else {
                                transactionsAdapter.setData(filteredList)
                                binding.tvTotal.text = transactionsAdapter.getTotal().toString()
                            }
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
    }

    private fun requestFirebaseData() {
        transactionsViewModel.getAllTransactions(account)
        transactionsViewModel.transactionResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        transactionsAdapter.setData(newData = it)
                        binding.tvTotal.text = transactionsAdapter.getTotal().toString()

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
            rvTransactions.adapter = transactionsAdapter
            rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}