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
import com.chrisojukwu.tallybookkeeping.databinding.FragmentEditInventoryItemBinding
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.chrisojukwu.tallybookkeeping.utils.checkIfValidNumber
import com.chrisojukwu.tallybookkeeping.utils.toTwoDP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditInventoryItemFragment : Fragment() {

    private lateinit var binding: FragmentEditInventoryItemBinding
    private val inventoryViewModel: InventoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditInventoryItemBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = inventoryViewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            editTextProductName.setText(inventoryViewModel.editStockItem.value?.stockName)
            editTextCostPrice.setText(inventoryViewModel.editStockItem.value?.costPrice?.toTwoDP().toString())
            editTextSellingPrice.setText(inventoryViewModel.editStockItem.value?.sellingPrice?.toTwoDP().toString())
            editTextQuantity.setText(inventoryViewModel.editStockItem.value?.quantity.toString())
        }
        binding.imageViewBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.editStockProductSaveButton.setOnClickListener {
            checkAllInputFields()
        }

        inventoryViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.editStockProductSaveButton.isEnabled = !loading
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
                inventoryViewModel.updateStockItem(
                    StockItem(
                        stockName = binding.editTextProductName.text.toString(),
                        sku = inventoryViewModel.editStockItem.value!!.sku,
                        costPrice = binding.editTextCostPrice.text.toString().toBigDecimal(),
                        sellingPrice = binding.editTextSellingPrice.text.toString().toBigDecimal(),
                        quantity = binding.editTextQuantity.text.toString().toInt(),
                        imageUrl = null
                    )
                )
                inventoryViewModel.setIsLoading(true)
                lifecycleScope.launch(Dispatchers.IO) {
                    inventoryViewModel.updateInventoryItem().collect { result ->
                        when (result) {
                            is Result.Success -> {
                                inventoryViewModel.setIsLoading(false)
                                Toast.makeText(requireContext(), "Entry updated!", Toast.LENGTH_LONG).show()
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