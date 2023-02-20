package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.anychart.AnyChartView
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.databinding.FragmentMainOverviewBinding
import com.serapercel.trickle.presentation.ui.viewModel.AccountViewModel
import com.serapercel.trickle.presentation.ui.viewModel.MainOverviewViewModel
import com.serapercel.trickle.presentation.ui.viewModel.TransactionsViewModel
import com.serapercel.trickle.util.createPieChart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainOverviewFragment @Inject constructor(
    var account: Account
) : Fragment() {
    private var _binding: FragmentMainOverviewBinding? = null
    private val binding get() = _binding!!

    private var chart: AnyChartView? = null
    var accountDataTemp = ArrayList<String>()
    var priceDataTemp = ArrayList<Float>()

    private lateinit var transactionsViewModel: TransactionsViewModel
    private lateinit var accountsViewModel: AccountViewModel

    private lateinit var mainOverviewViewModel: MainOverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainOverviewBinding.inflate(inflater, container, false)

        transactionsViewModel =
            ViewModelProvider(requireActivity())[TransactionsViewModel::class.java]
        accountsViewModel = ViewModelProvider(requireActivity())[AccountViewModel::class.java]

        mainOverviewViewModel = ViewModelProvider(this)[MainOverviewViewModel::class.java]

        mainOverviewViewModel.transactionsViewModel = transactionsViewModel
        mainOverviewViewModel.accountsViewModel = accountsViewModel

        lifecycleScope.launchWhenStarted {
            mainOverviewViewModel.getAllTransactions(account)
            delay(2000)
            priceDataTemp = mainOverviewViewModel.priceData
            accountDataTemp = mainOverviewViewModel.accounts
            delay(2000)
            chart = binding.pieChartMainOverview

            requireContext().createPieChart(
                chart!!,
                "Total Trickle",
                accountDataTemp,
                priceDataTemp
            )
        }

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}