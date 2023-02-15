package com.serapercel.trickle.common.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.trickle.R
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.databinding.LastTransactionCardBinding
import com.serapercel.trickle.presentation.ui.viewModel.TransactionsViewModel
import com.serapercel.trickle.util.DataDiffUtil
import com.serapercel.trickle.util.toastShort
import javax.inject.Inject

class TransactionAdapter @Inject constructor(
    var context: Context,
    var viewModel: TransactionsViewModel,
    var account: Account
) : RecyclerView.Adapter<TransactionAdapter.LastTransactionCardHolder>() {

    var transactionList = emptyList<ITransaction>()


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

        holder.binding.cvTransaction.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
            builder.setMessage("Do you want to delete this transaction?\n\n Title: ${lastTransactionItem.title}\n Price: ${lastTransactionItem.price}\n Account: ${lastTransactionItem.account.name}")
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                viewModel.deleteTransaction(lastTransactionItem, account)
                context.toastShort("Transaction deleted!")

            }
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
                context.toastShort("clicked cancel\n operation cancel")
            }
            builder.setNegativeButton("No") { dialogInterface, which ->
                context.toastShort("clicked No")
            }
            builder.show()
            true
        }

        if (lastTransactionItem.income) {
            holder.binding.ivLastTransactionIcon.setImageResource(R.drawable.ic_arrow_drop_up)
        } else {
            holder.binding.ivLastTransactionIcon.setImageResource(R.drawable.ic_arrow_drop_down)
        }
    }

    override fun getItemCount(): Int = transactionList.size

    fun setData(newData: List<ITransaction>) {
        if (newData.isNotEmpty()) {
            val transactionsDiffUtil = DataDiffUtil(transactionList, newData)
            val diffUtilResult = DiffUtil.calculateDiff(transactionsDiffUtil)
            transactionList = newData
            diffUtilResult.dispatchUpdatesTo(this)
            Log.e("hata", "not empty")
        } else {
            context.toastShort("Not Found")
            Log.e("hata", " empty")

        }
    }

}