package com.chrisojukwu.tallybookkeeping.ui.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.domain.model.StockItem
import com.chrisojukwu.tallybookkeeping.databinding.FragmentAddNewInventoryItemBinding
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.chrisojukwu.tallybookkeeping.utils.checkIfValidNumber
import com.chrisojukwu.tallybookkeeping.utils.getRandomSKU
import kotlinx.coroutines.launch

class AddNewInventoryItemFragment : Fragment() {

    private lateinit var binding: FragmentAddNewInventoryItemBinding
    private val inventoryViewModel: InventoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddNewInventoryItemBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        binding.imageViewBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newStockProductSaveButton.setOnClickListener {
            checkAllInputFields()
        }

        inventoryViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.newStockProductSaveButton.isEnabled = !loading
        }
    }

    private fun checkAllInputFields() {

        when {
            (binding.editTextProductName.text.isBlank()) -> {
                binding.editTextProductName.error = "Input required"
            }
            (!binding.editTextCostPrice.text.toString().checkIfValidNumber()) -> {
                binding.editTextCostPrice.error = "Invalid input"
            }
            (!binding.editTextSellingPrice.text.toString().checkIfValidNumber()) -> {
                binding.editTextSellingPrice.error = "Invalid input"
            }
            (!binding.editTextQuantity.text.toString().checkIfValidNumber()) -> {
                binding.editTextQuantity.error = "Invalid input"
            }
            else -> {
                inventoryViewModel.setIsLoading(true)
                lifecycleScope.launch {
                    inventoryViewModel.saveNewStockItem(
                        StockItem(
                            stockName = binding.editTextProductName.text.toString(),
                            sku = getRandomSKU(),
                            costPrice = binding.editTextCostPrice.text.toString().toBigDecimal(),
                            sellingPrice = binding.editTextSellingPrice.text.toString().toBigDecimal(),
                            quantity = binding.editTextQuantity.text.toString().toInt(),
                            imageUrl = null
                        )
                    ).collect { result ->
                        when (result) {
                            is Result.Success -> {
                                inventoryViewModel.setIsLoading(false)
                                Toast.makeText(requireContext(), "Stock added", Toast.LENGTH_LONG).show()
                                findNavController().navigateUp()
                            }
                            is Result.Error -> {
                                inventoryViewModel.setIsLoading(false)
                                Toast.makeText(requireContext(), "Error - please try again", Toast.LENGTH_LONG).show()
                            }
                            is Result.Loading -> {}
                        }
                    }
                }
            }
        }
    }

}