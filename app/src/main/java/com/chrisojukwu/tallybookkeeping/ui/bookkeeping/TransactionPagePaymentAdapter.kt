package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.data.models.Payment
import com.chrisojukwu.tallybookkeeping.databinding.IncomeDetailsPaymentHistoryBinding

class TransactionPagePaymentAdapter(var list: List<Payment>) :
    RecyclerView.Adapter<TransactionPagePaymentAdapter.ViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = IncomeDetailsPaymentHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payment = list[position]
        holder.bind(payment)
    }

    class ViewHolder(val binding: IncomeDetailsPaymentHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(payment: Payment) {
            binding.paymentRecord = payment
        }
    }

}