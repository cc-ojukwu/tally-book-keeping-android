package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.domain.model.Product
import com.chrisojukwu.tallybookkeeping.databinding.ReceiptItemBinding

class ReceiptPageItemAdapter(var list: List<Product>) :
    RecyclerView.Adapter<ReceiptPageItemAdapter.ReceiptItemViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptItemViewHolder {
        val view = ReceiptItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReceiptItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReceiptItemViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    class ReceiptItemViewHolder(val binding: ReceiptItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productItem = product
        }
    }

}