package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.databinding.FragmentAllRecordsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllRecordsFragment : Fragment() {

    private lateinit var binding: FragmentAllRecordsBinding
    private val viewModel: AllRecordsViewModel by viewModels()
    private val editIncomeViewModel: EditIncomeViewModel by activityViewModels()
    private val editExpenseViewModel: EditExpenseViewModel by activityViewModels()
    private val ieViewModel: IncomeExpenseDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllRecordsBinding.inflate(inflater, container, false).apply {
            allRecordsViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        requireActivity().window.statusBarColor = requireActivity().resources.getColor(R.color.background_color1, null)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewAllRecords.adapter =
            AllRecordsIncomeExpenseAdapter(
                { record -> onRecordClick(record) },
                { record -> onEditClick(record) }
            )

        binding.imageViewBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.sortedRecordsList.observe(viewLifecycleOwner) {
            (binding.recyclerViewAllRecords.adapter as AllRecordsIncomeExpenseAdapter)
                .allRecordsList = it
        }

    }

    private fun onEditClick(record: RecordHolder) {
        when (record) {
            is RecordHolder.Income -> {
                editIncomeViewModel.setRecordToEdit(record)
                findNavController().navigate(R.id.action_allRecordsFragment_to_editIncomeFragment)
            }
            is RecordHolder.Expense -> {
                editExpenseViewModel.setRecordToEdit(record)
                findNavController().navigate(R.id.action_allRecordsFragment_to_editExpenseFragment)
            }
            else -> {}
        }
    }

    private fun onRecordClick(record: RecordHolder) {
        ieViewModel.setRecord(record)
        findNavController().navigate(R.id.action_allRecordsFragment_to_incomeExpenseDetailsFragment)
    }
}