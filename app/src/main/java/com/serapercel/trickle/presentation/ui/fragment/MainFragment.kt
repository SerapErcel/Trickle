package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.serapercel.trickle.R
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.serapercel.trickle.common.adapter.OverViewPagerAdapter
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.data.source.remote.XMLResult
import com.serapercel.trickle.databinding.FragmentMainBinding
import com.serapercel.trickle.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment @Inject constructor(
    var account: Account
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

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val xml = XMLResult()
        val currencyList = xml.xmlDoviz()

        val currencyNameList = mutableListOf<String>()
        currencyList.forEach { currencyNameList.add(it.Isim) }

        val adapter2: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencyNameList)
        binding.spnCurrency.adapter = adapter2

        binding.spnCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.tvSpAlis.text = currencyList[position].ForexBuying
                binding.tvSpSatis.text = currencyList[position].ForexSelling
                binding.tvBankaAlis.text = currencyList[position].BanknoteBuying
                binding.tvBankaSatis.text = currencyList[position].BanknoteSelling

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val fragmentList = arrayListOf<Fragment>(
            MainOverviewFragment(account),
            UserOverviewFragment(account),
            TotalOverviewFragment(account)
        )
        val adapter =
            OverViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)
        binding.fragmentContainerOverview.adapter = adapter

        replaceFragment(
            requireActivity(),
            R.id.fragmentContainerNeeds,
            NeedsOverviewFragment(account.user)
        )
        replaceFragment(
            requireActivity(),
            R.id.fragmentContainerTransactions,
            LastTransactionsFragment(account)
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