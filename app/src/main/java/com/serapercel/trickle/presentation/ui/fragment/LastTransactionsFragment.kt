package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.serapercel.trickle.R
import com.serapercel.trickle.common.adapter.LastTransactionAdapter
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.databinding.FragmentLastTransactionsBinding
import com.serapercel.trickle.presentation.ui.viewModel.LastTransactionsViewModel
import com.serapercel.trickle.util.NetworkListener
import com.serapercel.trickle.util.NetworkResult
import com.serapercel.trickle.util.toastShort
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LastTransactionsFragment @Inject constructor(
    var account: Account
) : Fragment() {

    private var _binding: FragmentLastTransactionsBinding? = null
    private val binding get() = _binding!!

    private lateinit var transactionsViewModel: LastTransactionsViewModel
    private val transactionsAdapter by lazy { LastTransactionAdapter(requireContext()) }

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
        transactionsViewModel = ViewModelProvider(this)[LastTransactionsViewModel::class.java]

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


        binding.btnLastTransactionFilter.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)

            popupMenu.setOnMenuItemClickListener { menuItem ->
                return@setOnMenuItemClickListener when (menuItem.itemId) {
                    R.id.income -> {
                        transactionsAdapter.setData(transactionsViewModel.filterIncome())
                        true
                    }
                    R.id.expense -> {
                        transactionsAdapter.setData(transactionsViewModel.filterExpense())
                        true
                    }
                    R.id.noFilter -> {
                        transactionsAdapter.setData(transactionsViewModel.transactionResponse.value!!.data!!)
                        true
                    }
                    else -> false
                }
            }

            popupMenu.menuInflater.inflate(R.menu.transaction_filter_menu, popupMenu.menu)
            try {
                val fieldPopup = PopupMenu::class.java.getDeclaredField("mPopup")
                fieldPopup.isAccessible = true
                val mPopup = fieldPopup.get(popupMenu)
                mPopup.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(mPopup, true)
            } catch (e: Exception) {
                Log.e("Main", "Error showing menu icons.")
            } finally {
                popupMenu.show()
            }

        }
    }

    private fun requestFirebaseData() {
        transactionsViewModel.getTransactions(account)
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