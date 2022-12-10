package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.data.models.*
import com.chrisojukwu.tallybookkeeping.databinding.RecordItemExpenseBinding
import com.chrisojukwu.tallybookkeeping.databinding.RecordItemHeaderBinding
import com.chrisojukwu.tallybookkeeping.databinding.RecordItemIncomeBinding


class IncomeExpenseAdapter(
    private val recordsList: MutableList<RecordHolder>,
    private val onRecordClick: (record: RecordHolder) -> Unit
) :
    ListAdapter<RecordHolder, RecyclerView.ViewHolder>(DiffCallback) {

    //val recordsList = mutableListOf<RecordHolder>()

    companion object DiffCallback : DiffUtil.ItemCallback<RecordHolder>() {
        override fun areItemsTheSame(oldItem: RecordHolder, newItem: RecordHolder): Boolean {
            return oldItem.recordId == newItem.recordId
        }

        override fun areContentsTheSame(oldItem: RecordHolder, newItem: RecordHolder): Boolean {
            return oldItem.date == newItem.date
        }

        private const val TYPE_INCOME = 0
        private const val TYPE_EXPENSE = 1
        private const val TYPE_HEADER = 2

    }


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
                val view = RecordItemIncomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                IncomeViewHolder(view)
            }
            TYPE_EXPENSE -> {
                val view = RecordItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ExpenseViewHolder(view)
            }
            else -> {
                val view = RecordItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
             0 -> (holder as IncomeViewHolder).bind(recordsList[position] as RecordHolder.Income)
             1 -> (holder as ExpenseViewHolder).bind(recordsList[position] as RecordHolder.Expense)
             2 -> (holder as HeaderViewHolder).bind(recordsList[position] as RecordHolder.Header)
        }
    }


    inner class IncomeViewHolder(val binding: RecordItemIncomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: RecordHolder.Income) {
            binding.incomeRecord = record
        }
    }

    inner class ExpenseViewHolder(val binding: RecordItemExpenseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: RecordHolder.Expense) {
            binding.expenseRecord = record
        }
    }

    inner class HeaderViewHolder(val binding: RecordItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: RecordHolder.Header) {
            binding.header = record
        }
    }

}