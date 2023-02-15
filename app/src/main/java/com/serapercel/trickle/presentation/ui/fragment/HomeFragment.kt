package com.serapercel.trickle.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.serapercel.trickle.R
import com.serapercel.trickle.databinding.FragmentHomeBinding
import com.serapercel.trickle.presentation.ui.viewModel.HomeViewModel
import com.serapercel.trickle.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.rotate_close_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.to_bottom_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            requireContext(),
            R.anim.from_bottom_anim
        )
    }

    private var clicked = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        val args = HomeFragmentArgs.fromBundle(bundle!!)
        val account = args.account

        replaceFragment(requireActivity(), R.id.mainContainer, MainFragment(account))
        binding.fab.setOnClickListener {
            fabButtonClicked()
        }
        binding.btnAddNeed.setOnClickListener {
            replaceFragment(requireActivity(), R.id.mainContainer, AddNeedFragment(account.user))
            fabButtonClicked()
        }
        binding.btnAddTransaction.setOnClickListener {
            replaceFragment(requireActivity(), R.id.mainContainer, AddTransactionFragment(account))
            fabButtonClicked()
        }
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.mainFragment -> replaceFragment(
                    requireActivity(),
                    R.id.mainContainer,
                    MainFragment(account)
                )
                R.id.needsFragment -> replaceFragment(
                    requireActivity(),
                    R.id.mainContainer,
                    NeedsFragment(account.user)
                )
                R.id.profileFragment -> replaceFragment(
                    requireActivity(),
                    R.id.mainContainer,
                    ProfileFragment()
                )
                R.id.analyticsFragment -> replaceFragment(
                    requireActivity(),
                    R.id.mainContainer,
                    TransactionFragment(account)
                )
                else -> {
                }
            }
            true
        }
        observeLiveData()
    }

    private fun fabButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.btnAddNeed.startAnimation(fromBottom)
            binding.btnAddTransaction.startAnimation(fromBottom)
            binding.fab.startAnimation(rotateOpen)
        } else {
            binding.btnAddNeed.startAnimation(toBottom)
            binding.btnAddTransaction.startAnimation(toBottom)
            binding.fab.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.btnAddNeed.visibility = View.VISIBLE
            binding.btnAddTransaction.visibility = View.VISIBLE
        } else {
            binding.btnAddNeed.visibility = View.INVISIBLE
            binding.btnAddTransaction.visibility = View.INVISIBLE
        }
    }

    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            binding.btnAddTransaction.isClickable = true
            binding.btnAddNeed.isClickable = true
        } else {
            binding.btnAddTransaction.isClickable = false
            binding.btnAddNeed.isClickable = false
        }
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