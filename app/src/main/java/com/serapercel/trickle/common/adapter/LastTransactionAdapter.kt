package com.serapercel.trickle.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.trickle.R
import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.databinding.LastTransactionCardBinding
import com.serapercel.trickle.util.NeedsDiffUtil
import javax.inject.Inject

class LastTransactionAdapter @Inject constructor(
) : RecyclerView.Adapter<LastTransactionAdapter.LastTransactionCardHolder>() {

    private var transactionList = emptyList<ITransaction>()


    class LastTransactionCardHolder(val binding: LastTransactionCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): LastTransactionCardHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LastTransactionCardBinding.inflate(layoutInflater, parent, false)
                return LastTransactionCardHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastTransactionCardHolder =
        LastTransactionCardHolder.from(parent)

    override fun onBindViewHolder(holder: LastTransactionCardHolder, position: Int) {
        val lastTransactionItem = transactionList[position]
        holder.binding.tvLastTransactionName.text = lastTransactionItem.title
        holder.binding.tvLastTransactionAmount.text = lastTransactionItem.price
        holder.binding.tvLastTransactionAccount.text = lastTransactionItem.account.name
        if (lastTransactionItem.income) {
            holder.binding.ivLastTransactionIcon.setImageResource(R.drawable.ic_arrow_drop_up)
        } else {
            holder.binding.ivLastTransactionIcon.setImageResource(R.drawable.ic_arrow_drop_down)
        }

    }

    override fun getItemCount(): Int = transactionList.size

    fun setData(newData: List<ITransaction>) {
        val needsDiffUtil = NeedsDiffUtil(transactionList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(needsDiffUtil)
        transactionList = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}