package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.chrisojukwu.tallybookkeeping.databinding.FragmentIncomeExpenseDetailsBinding

class IncomeExpenseDetailsFragment : Fragment() {

    private lateinit var binding: FragmentIncomeExpenseDetailsBinding
    private val sharedViewModel: IncomeExpenseDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentIncomeExpenseDetailsBinding.inflate(layoutInflater, container, false).apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.itemsRecyclerView.adapter = TransactionPageItemAdapter(sharedViewModel.itemList.value!!)

        binding.paymentsRecyclerView.adapter = TransactionPagePaymentAdapter(sharedViewModel.paymentList.value!!)

        binding.imageViewBackButton.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        sharedViewModel.isFullyPaid.observe(viewLifecycleOwner) {
            if (it) {
                binding.textViewFullyPaid.visibility = View.VISIBLE
                binding.textViewPartlyPaid.visibility = View.GONE
            }else {
                binding.textViewFullyPaid.visibility = View.GONE
                binding.textViewPartlyPaid.visibility = View.VISIBLE
            }
        }


    }
}