package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.databinding.RecordDetailsPaymentHistoryBinding
import com.chrisojukwu.tallybookkeeping.domain.model.Payment

class IncomeExpenseDetailsPagePaymentAdapter(
    private val list: List<Payment>,
    private val onReceiptClick: (payment: Payment) -> Unit
) :
    RecyclerView.Adapter<IncomeExpenseDetailsPagePaymentAdapter.ViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RecordDetailsPaymentHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payment = list[position]
        holder.bind(payment)
        holder.binding.textViewReceipt.setOnClickListener {
            onReceiptClick(payment)
        }
    }

    class ViewHolder(val binding: RecordDetailsPaymentHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(payment: Payment) {
            binding.paymentRecord = payment
        }
    }

}