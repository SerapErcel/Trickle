package com.serapercel.trickle.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.trickle.R
import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.databinding.LastTransactionCardBinding

class LastTransactionAdapter(
    var context: Context,
    var transactionList: ArrayList<ITransaction>
) : RecyclerView.Adapter<LastTransactionAdapter.LastTransactionCardHolder>() {
    inner class LastTransactionCardHolder(binding: LastTransactionCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: LastTransactionCardBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastTransactionCardHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = LastTransactionCardBinding.inflate(from, parent, false)
        return LastTransactionCardHolder(binding)
    }

    override fun onBindViewHolder(holder: LastTransactionCardHolder, position: Int) {
        val lastTransactionItem = transactionList[position]
        holder.binding.tvLastTransactionName.text = lastTransactionItem.title
        holder.binding.tvLastTransactionAmount.text = lastTransactionItem.price.toString()
        holder.binding.tvLastTransactionAccount.text = lastTransactionItem.account.name
        if (lastTransactionItem.income) {
            holder.binding.ivLastTransactionIcon.setImageResource(R.drawable.ic_arrow_drop_up)
        } else {
            holder.binding.ivLastTransactionIcon.setImageResource(R.drawable.ic_arrow_drop_down)
        }

    }

    override fun getItemCount(): Int = transactionList.size

}