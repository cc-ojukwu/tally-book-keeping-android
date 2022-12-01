package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.data.models.Product
import com.chrisojukwu.tallybookkeeping.databinding.ProductItemBinding

class EditIncomeProductListAdapter() : RecyclerView.Adapter<EditIncomeProductListAdapter.EditIncomeProductListViewHolder>() {

    private var productList = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditIncomeProductListViewHolder {
        val view = ProductItemBinding.inflate(LayoutInflater.from(parent.context))
        return EditIncomeProductListViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditIncomeProductListViewHolder, position: Int) {
        val productItem = productList[position]
        holder.bind(productItem)
    }

    override fun getItemCount(): Int = productList.size

    fun updateItems(items: MutableList<Product>?) {
        productList = items ?: mutableListOf()
        notifyDataSetChanged()
    }

    class EditIncomeProductListViewHolder(var binding: ProductItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.productItem = product
        }
    }
}