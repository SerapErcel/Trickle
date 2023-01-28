package com.serapercel.trickle.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.databinding.NeedsCardBinding
import com.serapercel.trickle.util.NeedsDiffUtil

class NeedsAdapter: RecyclerView.Adapter<NeedsAdapter.NeedsViewHolder>() {
    private var needs = emptyList<Need>()
    class NeedsViewHolder(val binding: NeedsCardBinding): RecyclerView.ViewHolder(binding.root){
        companion object {
            fun from(parent: ViewGroup): NeedsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NeedsCardBinding.inflate(layoutInflater, parent, false)
                return NeedsViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NeedsViewHolder =NeedsViewHolder.from(parent)

    override fun getItemCount(): Int = needs.size

    override fun onBindViewHolder(holder: NeedsViewHolder, position: Int) {
        val currentNeed = needs[position]
        holder.binding.tvNeedsCount.text= currentNeed.count
        holder.binding.tvNeedsName.text= currentNeed.name
    }

    fun setData(newData: List<Need>) {
        val recipesDiffUtil = NeedsDiffUtil(needs, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        needs = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }

}