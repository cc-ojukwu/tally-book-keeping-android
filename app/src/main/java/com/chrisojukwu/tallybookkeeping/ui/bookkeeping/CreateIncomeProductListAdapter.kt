package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.domain.model.Product
import com.chrisojukwu.tallybookkeeping.databinding.IncomeProductItemBinding


class CreateIncomeProductListAdapter(
    var list: MutableList<Product> = mutableListOf<Product>(),
    private val onDeleteClick: (item: Product) -> Unit,
    private val onEditClick: (item: Product) -> Unit
) :
    RecyclerView.Adapter<CreateIncomeProductListAdapter.CreateIncomeProductListViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateIncomeProductListViewHolder {
        val view = IncomeProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreateIncomeProductListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreateIncomeProductListViewHolder, position: Int) {
        val productItem = list[position]
        holder.bind(productItem)
        holder.binding.deleteButton.setOnClickListener {
            onDeleteClick(productItem)
        }
        holder.binding.editButton.setOnClickListener {
            onEditClick(productItem)
        }
    }

    fun updateItems(items: MutableList<Product>?) {
        list = items ?: mutableListOf()
        notifyDataSetChanged()
    }

    class CreateIncomeProductListViewHolder(val binding: IncomeProductItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productItem = product
        }
    }

}