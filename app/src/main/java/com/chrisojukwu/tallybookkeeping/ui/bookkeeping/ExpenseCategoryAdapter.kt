package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.databinding.ExpenseCategoryItemBinding

class ExpenseCategoryAdapter(val onClickCategory: (string: String) -> Unit) :
    RecyclerView.Adapter<ExpenseCategoryAdapter.ViewHolder>() {

    private var categoryList = expenseCategoryList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ExpenseCategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = categoryList[position]
        holder.binding.textViewCategoryItem.setOnClickListener {
            onClickCategory(item)
        }
        holder.bind(item)

    }

    override fun getItemCount(): Int = categoryList.size

    fun filterList(newList: MutableList<String>) {
        // below line is to add our filtered list in our course array list.
        categoryList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ExpenseCategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (item: String) {
            binding.textViewCategoryItem.text = item
        }
    }

}

val expenseCategoryList = mutableListOf("Advances","Advertising", "Bank fees and charges", "Branding", "Daily Savings", "Depreciation", "Damages",
    "Employee Benefits", "Entertainment", "Equipment", "Electricity", "Furniture and Fittings","Interest Payments", "Investment", "IT and Internet", "Loans",
    "Licenses", "Marketing", "Meals", "Miscellaneous", "Office Supplies", "Payroll", "Packaging Materials", "Plant and Machinery",
    "Postage", "Professional Fees", "Printing", "Purchase Discounts", "Raw Materials",  "Refund",
    "Repairs and Maintenance", "Recurring Expense", "Salaries and Wages","Service Fees", "Software",
    "Supplies", "Taxes", "Travel Expenses", "Transport","Utilities")