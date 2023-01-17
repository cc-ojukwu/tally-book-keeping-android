package com.chrisojukwu.tallybookkeeping.ui.debt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.domain.model.Receivable
import com.chrisojukwu.tallybookkeeping.databinding.ReceivableItemBinding

class ReceivableAdapter : RecyclerView.Adapter<ReceivableAdapter.ReceivableViewHolder>() {

    private val differ = AsyncListDiffer(this, DiffCallback)
    var receivableList: List<Receivable> = listOf()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivableViewHolder {
        val view = ReceivableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReceivableViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceivableViewHolder, position: Int) {
        val item = receivableList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class ReceivableViewHolder(val binding: ReceivableItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(receivable: Receivable) {
            binding.receivable = receivable
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Receivable>() {
        override fun areItemsTheSame(oldItem: Receivable, newItem: Receivable): Boolean {
            return oldItem.recordId == newItem.recordId
        }

        override fun areContentsTheSame(oldItem: Receivable, newItem: Receivable): Boolean {
            return oldItem.date == newItem.date
        }

    }
}