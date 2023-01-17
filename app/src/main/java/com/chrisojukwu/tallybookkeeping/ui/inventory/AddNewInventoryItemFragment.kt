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
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.domain.model.InventoryItem
import com.chrisojukwu.tallybookkeeping.databinding.FragmentAddNewInventoryItemBinding
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.chrisojukwu.tallybookkeeping.utils.checkIfValidNumber
import com.chrisojukwu.tallybookkeeping.utils.getRandomSKU
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddNewInventoryItemFragment : Fragment() {

    private lateinit var binding: FragmentAddNewInventoryItemBinding
    private val inventoryViewModel: InventoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddNewInventoryItemBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        requireActivity().window.statusBarColor = requireActivity().resources.getColor(R.color.background_color1, null)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageViewBackButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.newStockProductSaveButton.setOnClickListener {
            checkAllInputFields()
        }

        inventoryViewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.newStockProductSaveButton.isEnabled = !loading
        }

        binding.plus.setOnClickListener {
            try {
                var newQty = binding.editTextQuantity.text.toString().toInt()
                binding.editTextQuantity.setText((++newQty).toString())
            } catch (_: Exception) { }
        }

        binding.minus.setOnClickListener {
            try {
                if (binding.editTextQuantity.text.toString().toInt() > 0) {
                    var newQty = binding.editTextQuantity.text.toString().toInt()
                    binding.editTextQuantity.setText((--newQty).toString())
                }
            } catch (_: Exception) { }
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
                        InventoryItem(
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
                                inventoryViewModel.getInventoryData()
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