package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.databinding.AllRecordsItemExpenseBinding
import com.chrisojukwu.tallybookkeeping.databinding.AllRecordsItemHeaderBinding
import com.chrisojukwu.tallybookkeeping.databinding.AllRecordsItemIncomeBinding
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder


class AllRecordsIncomeExpenseAdapter(
    private val onRecordClick: (record: RecordHolder) -> Unit,
    private val onEditClick: (record: RecordHolder) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ = AsyncListDiffer(this, IncomeExpenseAdapter)
    var allRecordsList: List<RecordHolder> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    companion object DiffCallback : DiffUtil.ItemCallback<RecordHolder>() {
        override fun areItemsTheSame(oldItem: RecordHolder, newItem: RecordHolder): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: RecordHolder, newItem: RecordHolder): Boolean {
            return oldItem.recordId == newItem.recordId
        }

        private const val TYPE_INCOME = 0
        private const val TYPE_EXPENSE = 1
        private const val TYPE_HEADER = 2

    }


    override fun getItemViewType(position: Int): Int {
        return when (allRecordsList[position]) {
            is RecordHolder.Income -> TYPE_INCOME
            is RecordHolder.Expense -> TYPE_EXPENSE
            is RecordHolder.Header -> TYPE_HEADER
        }
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_INCOME -> {
                val view = AllRecordsItemIncomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                IncomeViewHolder(view)
            }
            TYPE_EXPENSE -> {
                val view = AllRecordsItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ExpenseViewHolder(view)
            }
            else -> {
                val view = AllRecordsItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> (holder as IncomeViewHolder).bind(allRecordsList[position] as RecordHolder.Income)

            1 -> (holder as ExpenseViewHolder).bind(allRecordsList[position] as RecordHolder.Expense)

            2 -> (holder as HeaderViewHolder).bind(allRecordsList[position] as RecordHolder.Header)
        }

    }


    inner class IncomeViewHolder(val binding: AllRecordsItemIncomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: RecordHolder.Income) {
            binding.incomeRecord = record
            binding.cardViewIncomeItem.setOnClickListener {
                onRecordClick(record)
            }
            binding.buttonEditRecord.setOnClickListener {
                onEditClick(record)
            }
        }
    }

    inner class ExpenseViewHolder(val binding: AllRecordsItemExpenseBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: RecordHolder.Expense) {
            binding.expenseRecord = record
            binding.cardViewExpenseItem.setOnClickListener {
                onRecordClick(record)
            }
            binding.buttonEditRecord.setOnClickListener {
                onEditClick(record)
            }
        }
    }

    inner class HeaderViewHolder(val binding: AllRecordsItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(record: RecordHolder.Header) {
            binding.header = record
        }
    }

}