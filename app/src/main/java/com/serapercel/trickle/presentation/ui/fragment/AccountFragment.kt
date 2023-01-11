package com.serapercel.trickle.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.serapercel.trickle.common.adapter.AccountAdapter
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.databinding.FragmentAccountBinding
import com.serapercel.trickle.presentation.ui.viewModel.AccountViewModel
import com.serapercel.trickle.util.toAccount
import com.serapercel.trickle.util.toastLong

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AccountViewModel
    private lateinit var accountAdapter: AccountAdapter
    private val args: AccountFragmentArgs by navArgs()
    private lateinit var email: String
    private lateinit var account: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        email = args.email.toString()

        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        viewModel.fetchAccounts(email)
        accountAdapter = AccountAdapter(requireActivity(), viewModel.user, arrayListOf())
        binding.accountRV.adapter = accountAdapter
        binding.accountRV.layoutManager = LinearLayoutManager(context)

        initClickListener()

    }

    private fun initClickListener() {
        observeLiveData()
        binding.fabNewAccount.setOnClickListener {
            binding.textInputLayout.visibility = View.VISIBLE
            binding.btnNewAccount.visibility = View.VISIBLE
        }
        binding.btnNewAccount.setOnClickListener {
            account = binding.etAccountName.text.toString()
            if (viewModel.addAccount(email, account)) {
                val newAccount = Account(account, viewModel.user)
                addSharedPref(newAccount)
                val action =
                    AccountFragmentDirections.actionAccountFragmentToHomeFragment2(newAccount)
                findNavController().navigate(action)
            } else {
                requireContext().toastLong("Add Account Error")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val sharedPreference =
            requireContext().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE)
        if (sharedPreference.contains("account")) {
            val action =
                AccountFragmentDirections.actionAccountFragmentToHomeFragment2(
                    getSharedPref(
                        requireContext()
                    )
                )
            findNavController().navigate(action)
        }
    }

    private fun getSharedPref(context: Context): Account {
        val sharedPreference = context.getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE)
        return sharedPreference.getString("account", "defVAlue is comming")!!.toAccount()
    }

    private fun observeLiveData() {
        viewModel.accounts.observe(viewLifecycleOwner) { accounts ->
            accounts?.let {
                binding.accountRV.visibility = View.VISIBLE
                accountAdapter.updateAccountList(accounts)
            }
        }

    }

    private fun addSharedPref(account: Account) {
        val sharedPreference =
            requireContext().getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val sharedPrefString =
            "${account.name} ${account.user!!.value!!.email} ${account.user.value!!.id}"
        editor.putString("account", sharedPrefString)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}