package com.serapercel.trickle.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.trickle.data.entity.Currency
import com.serapercel.trickle.databinding.CurrencyCardBinding

class CurrencyAdapter (
    var context: Context,
    var currencyList: ArrayList<Currency>
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyCardHolder>() {
    inner class CurrencyCardHolder(binding: CurrencyCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: CurrencyCardBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyCardHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = CurrencyCardBinding.inflate(from, parent, false)
        return CurrencyCardHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyCardHolder, position: Int) {
        val currencyItem = currencyList[position]
        holder.binding.tvCurrencyName.text = currencyItem.name
        holder.binding.tvCurrencyPrice.text = currencyItem.price.toString()

    }

    override fun getItemCount(): Int = currencyList.size

}