package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.data.models.*
import com.chrisojukwu.tallybookkeeping.databinding.RecordItemExpenseBinding
import com.chrisojukwu.tallybookkeeping.databinding.RecordItemHeaderBinding
import com.chrisojukwu.tallybookkeeping.databinding.RecordItemIncomeBinding


class IncomeExpenseAdapter(
    private val recordsList: MutableList<RecordHolder>,
    private val onRecordClick: (record: RecordHolder) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemViewType(position: Int): Int {
        return when (recordsList[position]) {
            is RecordHolder.Income -> TYPE_INCOME
            is RecordHolder.Expense -> TYPE_EXPENSE
            is RecordHolder.Header -> TYPE_HEADER
        }
    }

    override fun getItemCount(): Int = recordsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_INCOME -> {
                val view = RecordItemIncomeBinding.inflate(LayoutInflater.from(parent.context))
                incomeViewHolder(view)
            }
            TYPE_EXPENSE -> {
                val view = RecordItemExpenseBinding.inflate(LayoutInflater.from(parent.context))
                expenseViewHolder(view)
            }
            else -> {
                val view = RecordItemHeaderBinding.inflate(LayoutInflater.from(parent.context))
                headerViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
             0 -> (holder as incomeViewHolder).bind(recordsList[position] as RecordHolder.Income)
             1 -> (holder as expenseViewHolder).bind(recordsList[position] as RecordHolder.Expense)
             2 -> (holder as headerViewHolder).bind(recordsList[position] as RecordHolder.Header)
        }
    }


    inner class incomeViewHolder(val binding: RecordItemIncomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: RecordHolder.Income) {
            binding.incomeRecord = record
        }
    }

    inner class expenseViewHolder(val binding: RecordItemExpenseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: RecordHolder.Expense) {
            binding.expenseRecord = record
        }
    }

    inner class headerViewHolder(val binding: RecordItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: RecordHolder.Header) {
            binding.header = record
        }
    }

    companion object {
        private const val TYPE_INCOME = 0
        private const val TYPE_EXPENSE = 1
        private const val TYPE_HEADER = 2
    }

//    class IncomeExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//
//        private fun bindIncome(item: RecordHolder.Income) {
//            bindingIncome.incomeRecord = item
//        }
//
//        private fun bindExpense(item: RecordHolder.Expense) {
//            bindingExpense.expenseRecord = item
//        }
//
//        private fun bindHeader(item: RecordHolder.Header) {
//            bindingHeader.bindingHeader.header = item
//        }
//
//        fun bind(recordHolder: RecordHolder) {
//            when (recordHolder) {
//                is RecordHolder.Income -> bindIncome(recordHolder)
//                is RecordHolder.Expense -> bindExpense(recordHolder)
//                is RecordHolder.Header -> bindHeader(recordHolder)
//            }
//        }
//    }


}