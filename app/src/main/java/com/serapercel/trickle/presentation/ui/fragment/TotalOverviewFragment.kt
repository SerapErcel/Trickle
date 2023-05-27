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
import com.serapercel.trickle.databinding.FragmentTotalOverviewBinding
import com.serapercel.trickle.presentation.ui.viewModel.TotalOverviewViewModel
import com.serapercel.trickle.presentation.ui.viewModel.TransactionsViewModel
import com.serapercel.trickle.util.createPieChart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class TotalOverviewFragment @Inject constructor(
    var account: Account
) : Fragment() {

    private var _binding: FragmentTotalOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var transactionsViewModel: TransactionsViewModel
    private lateinit var totalOverviewViewModel: TotalOverviewViewModel

    private var chart: AnyChartView? = null
    var labelData = ArrayList<String>()
    var priceData = ArrayList<Float>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTotalOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionsViewModel =
            ViewModelProvider(requireActivity())[TransactionsViewModel::class.java]
        totalOverviewViewModel =
            ViewModelProvider(requireActivity())[TotalOverviewViewModel::class.java]

        totalOverviewViewModel.transactionsViewModel = transactionsViewModel

        lifecycleScope.launchWhenStarted {
            transactionsViewModel.getAllTransactions(account)
            delay(2000)

            totalOverviewViewModel.getTransactions(account)
            delay(2000)

            priceData = totalOverviewViewModel.priceData
            delay(2000)

            labelData.add("Total Income")
            labelData.add("Total Expense")

            chart = binding.pieChartTotalOverview
            try {
                requireContext().createPieChart(chart!!, "Bank Statement", labelData, priceData)
            } catch (e: Exception) {
                binding.pieChartTotalOverview.visibility = View.INVISIBLE
                binding.tvPieError.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}