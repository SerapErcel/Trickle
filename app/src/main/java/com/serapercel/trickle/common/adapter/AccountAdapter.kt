package com.serapercel.trickle.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.databinding.CardAccountBinding
import com.serapercel.trickle.presentation.ui.fragment.AccountFragmentDirections

class AccountAdapter(
    var context: Context,
    var user: MutableLiveData<User>,
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

        holder.binding.accountCard.setOnClickListener {
            val newAccount = Account(account, user)
            addSharedPref(newAccount)
            val action = AccountFragmentDirections.actionAccountFragmentToHomeFragment2(newAccount)
            Navigation.findNavController(it).navigate(action)

            /*val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("account", account)
            activity.startActivity(intent)
            activity.finish()*/
        }
    }

    override fun getItemCount(): Int = accountList.size

    private fun addSharedPref(account: Account) {
        val sharedPreference =
            context.getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val sharedPrefString = "${account.name} ${account.user.value!!.email} ${account.user.value!!.id}"
        editor.putString("account", sharedPrefString)
        editor.apply()
    }

    fun updateAccountList(newAccountList: List<String>) {
        accountList.clear()
        accountList.addAll(newAccountList)
        notifyDataSetChanged()
    }
}