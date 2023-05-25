package com.serapercel.trickle.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.databinding.CardAccountBinding
import com.serapercel.trickle.presentation.ui.fragment.AccountFragmentDirections
import com.serapercel.trickle.presentation.ui.viewModel.AccountViewModel

class AccountAdapter(
    var context: Context,
    var viewModel: AccountViewModel,
    var accountList: ArrayList<String>
) : RecyclerView.Adapter<AccountAdapter.CardAccountHolder>() {
    inner class CardAccountHolder(binding: CardAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: CardAccountBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardAccountHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CardAccountBinding.inflate(from, parent, false)
        return CardAccountHolder(binding)
    }

    override fun onBindViewHolder(holder: CardAccountHolder, position: Int) {
        val account = accountList[position]
        holder.binding.twAccountName.text = account
        val newAccount = Account(account, viewModel.user.value!!)

        holder.binding.accountCard.setOnClickListener {
            addSharedPref(newAccount)
            val action = AccountFragmentDirections.actionAccountFragmentToHomeFragment2(newAccount)
            Navigation.findNavController(it).navigate(action)
        }
        holder.binding.ivDelete.setOnClickListener {
            viewModel.deleteAccount(viewModel.user.value!!.email!!, newAccount.name )
        }
    }

    override fun getItemCount(): Int = accountList.size

    private fun addSharedPref(account: Account) {
        val sharedPreference =
            context.getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val sharedPrefString = "${account.name} ${account.user.email} ${account.user.id}"
        editor.putString("account", sharedPrefString)
        editor.apply()
    }

    fun updateAccountList(newAccountList: List<String>) {
        accountList.clear()
        accountList.addAll(newAccountList)
        notifyDataSetChanged()
    }
}