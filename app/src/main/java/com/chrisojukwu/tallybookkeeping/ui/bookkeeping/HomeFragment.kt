package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.data.models.RecordHolder
import com.chrisojukwu.tallybookkeeping.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val sharedViewModel: IncomeExpenseDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            sharedViewmodel = viewModel
        }

        (activity as AppCompatActivity?)!!.supportActionBar?.hide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter =
            IncomeExpenseAdapter(viewModel.displayList.value!!) { transaction -> onTransactionClick(transaction) }
        binding.recyclerViewRecords.adapter = adapter

        binding.cardViewMoneyIn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_editIncomeFragment)
        }

        binding.cardViewMoneyOut.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_editExpenseFragment)
        }

    }

    private fun onTransactionClick(transaction: RecordHolder) {
        val record = viewModel.getRecordUsingId(transaction.recordId)
        if (record != null) sharedViewModel.setRecord(record)
        findNavController().navigate(R.id.action_homeFragment_to_incomeExpenseDetailsFragment)
    }
}