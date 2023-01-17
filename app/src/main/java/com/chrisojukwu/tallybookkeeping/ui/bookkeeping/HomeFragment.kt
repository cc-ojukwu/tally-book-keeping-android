package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val receiptViewModel: ReceiptViewModel by activityViewModels()
    private val editIncomeViewModel: EditIncomeViewModel by activityViewModels()
    private val editExpenseViewModel: EditExpenseViewModel by activityViewModels()
    private val ieViewModel: IncomeExpenseDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            sharedViewModel = viewModel
        }
        requireActivity().window.statusBarColor = requireActivity().resources.getColor(R.color.primary_color, null)

        (activity as AppCompatActivity?)!!.supportActionBar?.hide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter =
            IncomeExpenseAdapter(
                { transaction -> onTransactionClick(transaction) },
                { transaction -> onTransactionEditClick(transaction) },
                { transaction -> onTransactionReceiptClick(transaction) })

        binding.recyclerViewRecords.adapter = adapter

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.nestedScrollView.visibility = View.GONE
                binding.loadingView.visibility = View.VISIBLE
            } else {
                binding.loadingView.visibility = View.GONE
                binding.nestedScrollView.visibility = View.VISIBLE
            }
        }

        viewModel.displayList.observe(viewLifecycleOwner) {
            (binding.recyclerViewRecords.adapter as IncomeExpenseAdapter)
                .recordsList = it
        }

        binding.cardViewMoneyIn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createIncomeFragment)
        }

        binding.cardViewMoneyOut.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createExpenseFragment)
        }

        binding.cardViewAllRecords.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_allRecordsFragment)
        }

        viewModel.responseReceived.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.displayList.observe(viewLifecycleOwner) { list ->
                    if (list.isEmpty()) {
                        binding.emptyList.visibility = View.VISIBLE
                    } else {
                        binding.emptyList.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun onTransactionClick(transaction: RecordHolder) {
        val record = viewModel.getRecordUsingId(transaction.recordId)
        if (record != null) ieViewModel.setRecord(record)
        findNavController().navigate(R.id.action_homeFragment_to_incomeExpenseDetailsFragment)
    }

    private fun onTransactionEditClick(transaction: RecordHolder) {
        val record = viewModel.getRecordUsingId(transaction.recordId)
        if (record != null) {
            when (record) {
                is RecordHolder.Income -> {
                    editIncomeViewModel.setRecordToEdit(record)
                    findNavController().navigate(R.id.action_homeFragment_to_editIncomeFragment)
                }
                is RecordHolder.Expense -> {
                    editExpenseViewModel.setRecordToEdit(record)
                    findNavController().navigate(R.id.action_homeFragment_to_editExpenseFragment)
                }
                else -> {}
            }
        }
    }

    private fun onTransactionReceiptClick(transaction: RecordHolder) {
        when (val record = viewModel.getRecordUsingId(transaction.recordId)) {
            is RecordHolder.Income -> {
                val payment = record.paymentList.find {
                    it.paymentDate == (transaction as RecordHolder.Income).date
                }
                receiptViewModel.setReceiptDetails(payment!!, record)
            }
            is RecordHolder.Expense -> {
                val payment = record.paymentList.find {
                    it.paymentDate == (transaction as RecordHolder.Expense).date
                }
                receiptViewModel.setReceiptDetails(payment!!, record)
            }
            else -> {}
        }
        findNavController().navigate(R.id.action_homeFragment_to_receiptFragment)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.refreshBusinessName()
        }
    }


}