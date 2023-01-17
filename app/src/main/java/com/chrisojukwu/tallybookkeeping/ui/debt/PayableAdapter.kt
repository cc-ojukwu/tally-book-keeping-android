package com.chrisojukwu.tallybookkeeping.ui.debt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.domain.model.Payable
import com.chrisojukwu.tallybookkeeping.databinding.PayableItemBinding

class PayableAdapter(): RecyclerView.Adapter<PayableAdapter.PayableViewHolder>() {

    private val differ = AsyncListDiffer(this, PayableAdapter)

    var payableList: List<Payable> = listOf()
    set(value) {
        field = value
        differ.submitList(value)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayableViewHolder {
        val view = PayableItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PayableViewHolder(view)
    }

    override fun onBindViewHolder(holder: PayableViewHolder, position: Int) {
        val item = payableList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class PayableViewHolder(val binding: PayableItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(payable: Payable) {
            binding.payable = payable
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Payable>() {
        override fun areItemsTheSame(oldItem: Payable, newItem: Payable): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Payable, newItem: Payable): Boolean {
            return oldItem.recordId == newItem.recordId
        }

    }
}