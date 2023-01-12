package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.serapercel.trickle.R
import com.serapercel.trickle.databinding.FragmentHomeBinding
import com.serapercel.trickle.presentation.ui.viewModel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        replaceFragment(MainFragment())
        binding.fab.setOnClickListener {
            replaceFragment(TransactionFragment())
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mainFragment -> replaceFragment(MainFragment())
                R.id.needsFragment -> replaceFragment(NeedsFragment())
                R.id.profileFragment -> replaceFragment(ProfileFragment())
                R.id.analyticsFragment -> replaceFragment(AnalyticsFragment())
                else -> {
                }
            }
            true
        }
        observeLiveData()
    }

    private fun replaceFragment(fragment: Fragment) {
        val manager = requireActivity().supportFragmentManager
        val fragmentTransaction = manager.beginTransaction()
        fragmentTransaction.replace(R.id.mainContainer, fragment)
        fragmentTransaction.commit()
    }

    private fun observeLiveData() {
        viewModel.getAccountData(requireContext())
        viewModel.accountLiveData.observe(viewLifecycleOwner, Observer { account ->
            account?.let {
                binding.tvToolbarAccountName.text = account.name
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}