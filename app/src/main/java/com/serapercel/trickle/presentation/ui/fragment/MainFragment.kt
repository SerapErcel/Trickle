package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serapercel.trickle.R
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.databinding.FragmentMainBinding
import com.serapercel.trickle.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment @Inject constructor(
    var user: User
) : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(requireActivity(), R.id.fragmentContainerOverview, MainOverviewFragment())
        replaceFragment(requireActivity(), R.id.fragmentContainerNeeds, NeedsOverviewFragment(user))
        replaceFragment(requireActivity(), R.id.fragmentContainerCurrency, CurrencyFragment())
        replaceFragment(
            requireActivity(),
            R.id.fragmentContainerTransactions,
            LastTransactionsFragment(user)
        )

        binding.btnCalculate.setOnClickListener {
            replaceFragment(requireActivity(), R.id.mainContainer, CalculatorFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}