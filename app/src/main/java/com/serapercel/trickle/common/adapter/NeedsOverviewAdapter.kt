package com.serapercel.trickle.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.trickle.databinding.NeedsOverviewCardBinding

class NeedsOverviewAdapter (
    var context: Context,
    var needList: ArrayList<String>
) : RecyclerView.Adapter<NeedsOverviewAdapter.NeedsOverviewCardHolder>() {
    inner class NeedsOverviewCardHolder(binding: NeedsOverviewCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: NeedsOverviewCardBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NeedsOverviewCardHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = NeedsOverviewCardBinding.inflate(from, parent, false)
        return NeedsOverviewCardHolder(binding)
    }

    override fun onBindViewHolder(holder: NeedsOverviewCardHolder, position: Int) {
        val needItem = needList[position]
        holder.binding.tvNeedsCardTitle.text = needItem
    }

    override fun getItemCount(): Int = needList.size

}