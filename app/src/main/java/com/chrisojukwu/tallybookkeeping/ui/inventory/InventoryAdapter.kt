package com.chrisojukwu.tallybookkeeping.ui.inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.domain.model.InventoryItem
import com.chrisojukwu.tallybookkeeping.databinding.InventoryItemBinding

class InventoryAdapter(
    val onStockItemClick: (inventoryItem: InventoryItem) -> Unit,
    val onEditClick: (inventoryItem: InventoryItem) -> Unit,
    val onAddStockClick: (inventoryItem: InventoryItem) -> Unit
): RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>() {

    private val differ = AsyncListDiffer(this, InventoryAdapter)
    var inventoryItemList: List<InventoryItem> = listOf()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val view = InventoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val item = inventoryItemList[position]
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

        fun bind(inventoryItem: InventoryItem) {
            binding.stockItem = inventoryItem
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<InventoryItem>() {
        override fun areItemsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
            return oldItem.sku == newItem.sku
        }

    }
}