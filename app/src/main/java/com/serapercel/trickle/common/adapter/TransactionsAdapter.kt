package com.serapercel.trickle.common.adapter

import android.content.Context
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
) : RecyclerView.Adapter<TransactionAdapter.TransactionCardHolder>() {

    private var transactionList = emptyList<ITransaction>()

    class TransactionCardHolder(val binding: LastTransactionCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): TransactionCardHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LastTransactionCardBinding.inflate(layoutInflater, parent, false)
                return TransactionCardHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionCardHolder =
        TransactionCardHolder.from(parent)

    override fun onBindViewHolder(holder: TransactionCardHolder, position: Int) {
        val lastTransactionItem = transactionList[position]

        holder.binding.tvLastTransactionName.text = lastTransactionItem.title
        holder.binding.tvLastTransactionAmount.text = lastTransactionItem.price
        holder.binding.tvLastTransactionAccount.text = lastTransactionItem.account.name

        holder.binding.cvTransaction.setOnLongClickListener {
            deleteTransaction(lastTransactionItem)
        }

        if (lastTransactionItem.income) {
            holder.binding.ivLastTransactionIcon.setImageResource(R.drawable.ic_arrow_drop_up)
        } else {
            holder.binding.ivLastTransactionIcon.setImageResource(R.drawable.ic_arrow_drop_down)
        }
    }

    override fun getItemCount(): Int = transactionList.size

    fun getTotal(): Double {
        var total = 0.0
        for (i in transactionList) {
            if (i.income) {
                total += i.price.toDouble()
            } else {
                total -= i.price.toDouble()
            }
        }
        return total
    }

    fun setData(newData: List<ITransaction>) {
        if (newData.isNotEmpty()) {
            val transactionsDiffUtil = DataDiffUtil(transactionList, newData)
            val diffUtilResult = DiffUtil.calculateDiff(transactionsDiffUtil)
            transactionList = newData
            diffUtilResult.dispatchUpdatesTo(this)
        } else {
            context.toastShort("Not Found")
        }
    }

    private fun deleteTransaction(transaction: ITransaction): Boolean {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete")
        builder.setMessage("Do you want to delete this transaction?\n\n Title: ${transaction.title}\n Price: ${transaction.price}\n Account: ${transaction.account.name}")
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            viewModel.deleteTransaction(transaction, account)
            if (viewModel.transactionResponse.value?.data.isNullOrEmpty()) {
                context.toastShort("Check the Internet Connection!")
            } else {
                val newList =
                    viewModel.transactionResponse.value!!.data!!.filter { it != transaction }
                setData(newList)
                context.toastShort("Transaction deleted!")
            }
        }
        builder.setNeutralButton("Cancel") { dialogInterface, which ->
            context.toastShort("clicked cancel\n operation cancel")
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            context.toastShort("clicked No")
        }
        builder.show()
        return true
    }
}