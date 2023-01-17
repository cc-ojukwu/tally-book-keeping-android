package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.chrisojukwu.tallybookkeeping.utils.Result
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.domain.model.Payment
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.databinding.FragmentIncomeExpenseDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.math.BigDecimal

@AndroidEntryPoint
class IncomeExpenseDetailsFragment : Fragment() {

    private lateinit var binding: FragmentIncomeExpenseDetailsBinding
    private val sharedViewModel: IncomeExpenseDetailsViewModel by activityViewModels()
    private val receiptViewModel: ReceiptViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentIncomeExpenseDetailsBinding.inflate(inflater, container, false).apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        requireActivity().window.statusBarColor = requireActivity().resources.getColor(R.color.background_color1, null)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.itemsRecyclerView.adapter = IncomeExpenseDetailsPageItemAdapter(sharedViewModel.itemList.value!!)

        binding.paymentsRecyclerView.adapter = IncomeExpenseDetailsPagePaymentAdapter(
            sharedViewModel.paymentList.value!!
        ) { payment -> onReceiptClick(payment) }

        binding.imageViewBackButton.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        binding.imageViewDeleteButton.setOnClickListener {
            openDeleteBottomSheet()
        }

        binding.textViewUpdatePayment.setOnClickListener {
            openUpdatePaymentBottomSheet()
        }

        sharedViewModel.isFullyPaid.observe(viewLifecycleOwner) {
            if (it) {
                binding.textViewFullyPaid.visibility = View.VISIBLE
                binding.textViewUpdatePayment.visibility = View.GONE
            } else {
                binding.textViewFullyPaid.visibility = View.GONE
                binding.textViewUpdatePayment.visibility = View.VISIBLE
            }
        }

    }

    private fun onReceiptClick(payment: Payment) {
        receiptViewModel.setReceiptDetails(payment, sharedViewModel.record.value)
        findNavController().navigate(R.id.action_incomeExpenseDetailsFragment_to_receiptFragment)
    }

    private fun openUpdatePaymentBottomSheet() {
        val updatePaymentBottomSheet = BottomSheetDialog(requireContext())

        updatePaymentBottomSheet.setContentView(R.layout.update_payment_bottomsheet)

        val editTextAmount = updatePaymentBottomSheet.findViewById<EditText>(R.id.edit_text_update_payment)
        val radioGroup = updatePaymentBottomSheet.findViewById<RadioGroup>(R.id.update_payment_radio_group)
        val saveButton = updatePaymentBottomSheet.findViewById<Button>(R.id.button_save_payment)

        var balanceDue: BigDecimal = BigDecimal.ZERO
        when (sharedViewModel.record.value) {
            is RecordHolder.Income -> {
                balanceDue = (sharedViewModel.record.value as RecordHolder.Income).balanceDue
                editTextAmount?.setText(balanceDue.toString())
            }
            is RecordHolder.Expense -> {
                balanceDue = (sharedViewModel.record.value as RecordHolder.Expense).balanceDue
                editTextAmount?.setText(balanceDue.toString())
            }
            else -> {}
        }

        radioGroup?.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.radio_button_fully) {
                editTextAmount?.setText(balanceDue.toString())
            } else {
                editTextAmount?.text?.clear()
            }
        }

        saveButton?.setOnClickListener {
            try {
                if (editTextAmount?.text.toString().toBigDecimal() > balanceDue) {
                    Toast.makeText(requireContext(), "Input amount higher than balance due.", Toast.LENGTH_SHORT).show()
                } else {
                    sharedViewModel.addNewPayment(editTextAmount?.text.toString().toBigDecimal())
                    callUpdateCollector()
                    updatePaymentBottomSheet.dismiss()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }

        updatePaymentBottomSheet.setCancelable(true)
        updatePaymentBottomSheet.show()
    }

    private fun callUpdateCollector() {
        lifecycleScope.launch {
            sharedViewModel.updateRecordResult.collect { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), "Payment added", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_incomeExpenseDetailsFragment_to_homeFragment)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Error. Please try later", Toast.LENGTH_LONG).show()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun openDeleteBottomSheet() {
        val deleteBottomSheetDialog = BottomSheetDialog(requireContext())

        deleteBottomSheetDialog.setContentView(R.layout.delete_transaction_bottomsheet)

        val deleteYes = deleteBottomSheetDialog.findViewById<TextView>(R.id.delete_yes_button)
        val deleteNo = deleteBottomSheetDialog.findViewById<TextView>(R.id.delete_no_button)

        deleteYes?.setOnClickListener {
            when (sharedViewModel.record.value) {
                is RecordHolder.Income -> {
                    deleteIncome()
                    deleteBottomSheetDialog.dismiss()
                }
                is RecordHolder.Expense -> {
                    deleteExpense()
                    deleteBottomSheetDialog.dismiss()
                }
                else -> {}
            }
        }
        deleteNo?.setOnClickListener {
            deleteBottomSheetDialog.dismiss()
        }

        deleteBottomSheetDialog.setCancelable(true)
        deleteBottomSheetDialog.show()
    }

    private fun deleteIncome() {
        lifecycleScope.launch {
            sharedViewModel.deleteIncome().collect { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), "Record deleted!", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_incomeExpenseDetailsFragment_to_homeFragment)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Error occurred", Toast.LENGTH_LONG).show()
                    }
                    is Result.Loading -> {}
                }

            }
        }
    }

    private fun deleteExpense() {
        lifecycleScope.launch {
            sharedViewModel.deleteExpense().collect { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), "Record deleted!", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_incomeExpenseDetailsFragment_to_homeFragment)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Error occurred", Toast.LENGTH_LONG).show()
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }
}