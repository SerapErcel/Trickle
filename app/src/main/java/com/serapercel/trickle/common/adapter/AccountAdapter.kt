package com.serapercel.trickle.common.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.trickle.databinding.CardAccountBinding

class AccountAdapter(
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
    }

    override fun getItemCount(): Int = accountList.size

    fun updateAccountList(newAccountList: List<String>){
        accountList.clear()
        accountList.addAll(newAccountList)
        notifyDataSetChanged()
    }
}