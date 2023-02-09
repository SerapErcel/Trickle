package com.serapercel.trickle.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.databinding.NeedsCardBinding
import com.serapercel.trickle.presentation.ui.viewModel.NeedsViewModel
import com.serapercel.trickle.util.NeedsDiffUtil
import com.serapercel.trickle.util.toastShort
import javax.inject.Inject

class NeedsAdapter @Inject constructor(
    private val needsViewModel: NeedsViewModel,
    private val requireActivity: FragmentActivity,
    val user: User
) :
    RecyclerView.Adapter<NeedsAdapter.NeedsViewHolder>() {

    private var needs = emptyList<Need>()

    class NeedsViewHolder(val binding: NeedsCardBinding) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): NeedsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NeedsCardBinding.inflate(layoutInflater, parent, false)
                return NeedsViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NeedsViewHolder =
        NeedsViewHolder.from(parent)

    override fun getItemCount(): Int = needs.size

    override fun onBindViewHolder(holder: NeedsViewHolder, position: Int) {
        val currentNeed = needs[position]
        holder.binding.tvNeedsCount.text = currentNeed.count
        holder.binding.tvNeedsName.text = currentNeed.name
        holder.binding.btnDeleteNeed.setOnClickListener {
            deleteNeed(currentNeed)
        }
    }

    private fun deleteNeed(need: Need) {

        if (needsViewModel.networkStatus) {
            needsViewModel.remoweNeed(need, user)
            needsViewModel.getNeeds(user)
            val newList = needs.filter { it != need }
            setData(newList)
        } else {
            requireActivity.toastShort("Check the Internet Connection!")
        }
    }

    fun setData(newData: List<Need>) {
        val needsDiffUtil = NeedsDiffUtil(needs, newData)
        val diffUtilResult = DiffUtil.calculateDiff(needsDiffUtil)
        needs = newData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}