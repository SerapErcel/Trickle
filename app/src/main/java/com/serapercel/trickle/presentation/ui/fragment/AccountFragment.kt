package com.serapercel.trickle.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.serapercel.trickle.common.adapter.AccountAdapter
import com.serapercel.trickle.databinding.FragmentAccountBinding
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.presentation.ui.activity.MainActivity
import com.serapercel.trickle.presentation.ui.viewModel.AccountViewModel
import com.serapercel.trickle.util.removePunctuation
import com.serapercel.trickle.util.toastLong
import com.serapercel.trickle.util.toastShort

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: AccountViewModel
    private lateinit var database: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var accountAdapter :AccountAdapter
    private val args: AccountFragmentArgs by navArgs()
    private lateinit var email: String
    private lateinit var userId: String
    private lateinit var user: User
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
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        viewModel.refreshData()

        accountAdapter = AccountAdapter(requireActivity(), arrayListOf())

        binding.accountRV.layoutManager = LinearLayoutManager(context)
        binding.accountRV.adapter = accountAdapter

        email = args.email.toString()
        database = FirebaseDatabase.getInstance()
        dbRef = database.getReference("accounts")
        initClickListener()
    }

    private fun initClickListener() {
        observeLiveData()
        binding.fabNewAccount.setOnClickListener {
            binding.textInputLayout.visibility = View.VISIBLE
            binding.btnNewAccount.visibility = View.VISIBLE
        }
        binding.btnNewAccount.setOnClickListener {
            userId = dbRef.push().key!!
            // user = User(userId, email)
            account = binding.etAccountName.text.toString()

            dbRef.child(user.email!!.removePunctuation()).child(account).setValue(user).addOnSuccessListener {
                val intent = Intent(activity, MainActivity::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            }.addOnFailureListener { exception ->
                exception.localizedMessage?.let { it -> requireContext().toastLong(it) }
            }

            binding.textInputLayout.visibility = View.INVISIBLE
            binding.btnNewAccount.visibility = View.INVISIBLE

        }
    }

    fun observeLiveData(){
        viewModel.accounts.observe(viewLifecycleOwner, Observer { accounts ->
            accounts?.let{
                binding.accountRV.visibility  = View.VISIBLE
                accountAdapter.updateAccountList(accounts)
            }
        })

        viewModel.accountsError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                if(it){
                    requireContext().toastShort("Error!\nPlease add new account.")
                    binding.textInputLayout.visibility = View.VISIBLE
                    binding.btnNewAccount.visibility = View.VISIBLE
                }else
                {
                    binding.accountRV.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}