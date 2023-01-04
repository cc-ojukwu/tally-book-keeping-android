package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.databinding.FragmentReceiptBinding

class ReceiptFragment : Fragment() {

    private lateinit var binding: FragmentReceiptBinding
    private val sharedViewModel: ReceiptViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReceiptBinding.inflate(inflater, container, false).apply {
            viewModel = sharedViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.receiptItemRecyclerView.adapter = ReceiptPageItemAdapter(sharedViewModel.itemList.value!!)

        binding.receiptPageBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}