package com.serapercel.trickle.common.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.trickle.databinding.CardAccountBinding
import com.serapercel.trickle.presentation.ui.activity.MainActivity

class AccountAdapter(
    var activity: Activity,
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
        holder.binding.twAccountName.text = accountList[position]

        holder.binding.accountCard.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    override fun getItemCount(): Int = accountList.size

    fun updateAccountList(newAccountList: List<String>){
        accountList.clear()
        accountList.addAll(newAccountList)
        notifyDataSetChanged()
    }
}