package com.serapercel.trickle.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.databinding.NeedsOverviewCardBinding
import com.serapercel.trickle.util.DataDiffUtil
import javax.inject.Inject

class NeedsOverviewAdapter @Inject constructor(
    val user: User
) : RecyclerView.Adapter<NeedsOverviewAdapter.NeedsOverviewCardHolder>() {

    private var needs = emptyList<Need>()

    class NeedsOverviewCardHolder(val binding: NeedsOverviewCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): NeedsOverviewCardHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NeedsOverviewCardBinding.inflate(layoutInflater, parent, false)
                return NeedsOverviewCardHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NeedsOverviewCardHolder =
        NeedsOverviewCardHolder.from(parent)


    override fun onBindViewHolder(holder: NeedsOverviewCardHolder, position: Int) {
        val needItem = needs[position]
        holder.binding.tvNeedsCardTitle.text = needItem.name
    }

    override fun getItemCount(): Int = needs.size

    fun setData(newData: List<Need>) {
        val diffUtil = DataDiffUtil(needs, newData)
        val diffUtilResult = DiffUtil.calculateDiff(diffUtil)
        needs = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}