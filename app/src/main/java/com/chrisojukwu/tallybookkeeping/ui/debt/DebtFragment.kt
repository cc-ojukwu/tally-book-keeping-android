package com.chrisojukwu.tallybookkeeping.ui.debt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.databinding.FragmentDebtBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DebtFragment : Fragment() {

    private lateinit var binding: FragmentDebtBinding
    private val viewModel: DebtViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentDebtBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            debtViewModel = viewModel
        }

        requireActivity().window.statusBarColor = requireActivity().resources.getColor(R.color.primary_color, null)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewReceivables.adapter = ReceivableAdapter()
        binding.recyclerViewPayables.adapter = PayableAdapter()

        viewModel.receivableList.observe(viewLifecycleOwner) {
            (binding.recyclerViewReceivables.adapter as ReceivableAdapter)
                .receivableList = it
        }

        viewModel.payableList.observe(viewLifecycleOwner) {
            (binding.recyclerViewPayables.adapter as PayableAdapter)
                .payableList = it
        }

        viewModel.responseReceived.observe(viewLifecycleOwner) {
            if (it) {
                viewModel.receivableList.observe(viewLifecycleOwner) { list ->
                    if (list.isEmpty()) {
                        binding.emptyListReceivables.visibility = View.VISIBLE
                    } else {
                        binding.emptyListReceivables.visibility = View.GONE
                    }
                }
                viewModel.payableList.observe(viewLifecycleOwner) { list ->
                    if (list.isEmpty()) {
                        binding.emptyListPayables.visibility = View.VISIBLE
                    } else {
                        binding.emptyListPayables.visibility = View.GONE
                    }
                }
            }
        }

    }

}