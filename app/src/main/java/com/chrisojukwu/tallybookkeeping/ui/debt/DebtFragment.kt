package com.chrisojukwu.tallybookkeeping.ui.debt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chrisojukwu.tallybookkeeping.databinding.FragmentDebtBinding
import com.chrisojukwu.tallybookkeeping.ui.bookkeeping.SharedTransactionsViewModel
import kotlinx.coroutines.launch

class DebtFragment : Fragment() {

    private lateinit var binding: FragmentDebtBinding
    private val viewModel: DebtViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val receivableAdapter = ReceivableAdapter()
        val payableAdapter = PayableAdapter()

        // Inflate the layout for this fragment
        binding = FragmentDebtBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            debtViewModel = viewModel
            recyclerViewReceivables.adapter = receivableAdapter
            recyclerViewPayables.adapter = payableAdapter
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.receivableList.observe(viewLifecycleOwner) {
            (binding.recyclerViewReceivables.adapter as ReceivableAdapter)
                .receivableList = it
        }

        viewModel.payableList.observe(viewLifecycleOwner) {
            (binding.recyclerViewPayables.adapter as PayableAdapter)
                .payableList = it
        }

    }

}