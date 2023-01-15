package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serapercel.trickle.R
import com.serapercel.trickle.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        replaceFragment(R.id.fragmentContainerOverview, MainOverviewFragment())
        replaceFragment(R.id.fragmentContainerNeeds, NeedsOverviewFragment())
        replaceFragment(R.id.fragmentContainerCurrency, CurrencyFragment())
        replaceFragment(R.id.fragmentContainerTransactions, LastTransactionsFragment())


    }
    private fun replaceFragment(container: Int,fragment: Fragment) {
        val manager = requireActivity().supportFragmentManager
        val fragmentTransaction = manager.beginTransaction()
        fragmentTransaction.replace(container, fragment)
        fragmentTransaction.commit()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}