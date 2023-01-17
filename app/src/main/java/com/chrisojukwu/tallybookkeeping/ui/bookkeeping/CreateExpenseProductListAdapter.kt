package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.domain.model.Product
import com.chrisojukwu.tallybookkeeping.databinding.ExpenseProductItemBinding


class CreateExpenseProductListAdapter(
    var list: MutableList<Product> = mutableListOf(),
    private val onDeleteClick: (item: Product) -> Unit,
    private val onEditClick: (item: Product) -> Unit
) :
    RecyclerView.Adapter<CreateExpenseProductListAdapter.CreateExpenseProductListViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateExpenseProductListViewHolder {
        val view = ExpenseProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreateExpenseProductListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreateExpenseProductListViewHolder, position: Int) {
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

    class CreateExpenseProductListViewHolder(val binding: ExpenseProductItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productItem = product
        }
    }

}
