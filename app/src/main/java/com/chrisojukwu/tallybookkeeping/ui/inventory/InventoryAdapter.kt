package com.chrisojukwu.tallybookkeeping.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.domain.model.StockItem
import com.chrisojukwu.tallybookkeeping.databinding.InventoryItemBinding

class InventoryAdapter(
    val onStockItemClick: (stockItem: StockItem) -> Unit,
    val onEditClick: (stockItem: StockItem) -> Unit,
    val onAddStockClick: (stockItem: StockItem) -> Unit
): RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {

    private val differ = AsyncListDiffer(this, InventoryAdapter)
    var stockItemList: List<StockItem> = listOf()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = InventoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val item = stockItemList[position]
        holder.bind(item)

        holder.binding.cardViewStockItem.setOnClickListener {
            onStockItemClick(item)
        }
        holder.binding.stockEditIcon.setOnClickListener {
            onEditClick(item)
        }
        holder.binding.textViewAddStock.setOnClickListener {
            onAddStockClick(item)
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    inner class InventoryViewHolder(val binding: InventoryItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(stockItem: StockItem) {
            binding.stockItem = stockItem
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<StockItem>() {
        override fun areItemsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: StockItem, newItem: StockItem): Boolean {
            return oldItem.sku == newItem.sku
        }

    }
}