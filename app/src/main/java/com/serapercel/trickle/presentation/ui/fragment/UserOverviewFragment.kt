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
import com.serapercel.trickle.databinding.FragmentUserOverviewBinding
import com.serapercel.trickle.presentation.ui.viewModel.LastTransactionsViewModel
import com.serapercel.trickle.presentation.ui.viewModel.UserOverviewViewModel
import com.serapercel.trickle.util.createPieChart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class UserOverviewFragment @Inject constructor(
    var account: Account
) : Fragment() {
    private var _binding: FragmentUserOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var lastTransactionsViewModel: LastTransactionsViewModel
    private lateinit var userOverviewViewModel: UserOverviewViewModel

    private var chart: AnyChartView? = null
    var labelData = ArrayList<String>()
    var priceData = ArrayList<Float>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lastTransactionsViewModel =
            ViewModelProvider(requireActivity())[LastTransactionsViewModel::class.java]
        userOverviewViewModel =
            ViewModelProvider(requireActivity())[UserOverviewViewModel::class.java]

        userOverviewViewModel.transactionsViewModel = lastTransactionsViewModel

        lifecycleScope.launchWhenStarted {
            lastTransactionsViewModel.getTransactions(account)
            userOverviewViewModel.getTransactions(account)
            delay(2000)

            priceData = userOverviewViewModel.priceData
            delay(2000)

            labelData.add("Income")
            labelData.add("Expense")

            chart = binding.pieChartUserOverview

            requireContext().createPieChart(
                chart!!,
                account.name.uppercase() + " Trickle",
                labelData,
                priceData
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}