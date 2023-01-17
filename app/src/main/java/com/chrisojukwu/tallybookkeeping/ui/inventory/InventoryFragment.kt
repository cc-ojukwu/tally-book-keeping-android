package com.chrisojukwu.tallybookkeeping.ui.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.domain.model.InventoryItem
import com.chrisojukwu.tallybookkeeping.databinding.FragmentInventoryBinding
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InventoryFragment : Fragment() {

    private lateinit var binding: FragmentInventoryBinding
    private val inventoryViewModel: InventoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentInventoryBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = inventoryViewModel
        }

        requireActivity().window.statusBarColor = requireActivity().resources.getColor(R.color.primary_color, null)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = InventoryAdapter({
            navigateToStockDetails(it)
        }, {
            navigateToStockEditPage(it)
        }, {
            openAddStockBottomSheet(it)
        })
        binding.recyclerViewInventory.adapter = adapter

        binding.addNewStockButton.setOnClickListener {
            findNavController().navigate(R.id.action_inventoryFragment_to_addNewInventoryItemFragment)
        }

        inventoryViewModel.inventoryList.observe(viewLifecycleOwner) { list ->
            (binding.recyclerViewInventory.adapter as InventoryAdapter)
                .inventoryItemList = list
        }

    }

    private fun navigateToStockDetails(inventoryItem: InventoryItem) {
        inventoryViewModel.setStockDetails(inventoryItem)
        findNavController().navigate(R.id.action_inventoryFragment_to_stockItemDetailsFragment)
    }

    private fun navigateToStockEditPage(inventoryItem: InventoryItem) {
        inventoryViewModel.setEditStockDetails(inventoryItem)
        findNavController().navigate(R.id.action_inventoryFragment_to_editInventoryItemFragment)
    }

    private fun openAddStockBottomSheet(inventoryItem: InventoryItem) {
        val addStockDialog = BottomSheetDialog(requireContext())

        addStockDialog.setContentView(R.layout.add_stock_bottomsheet)

        val stockName = addStockDialog.findViewById<TextView>(R.id.stock_item_name)
        val stockPrice = addStockDialog.findViewById<TextView>(R.id.stock_item_selling_price)
        val stockQtyLeft = addStockDialog.findViewById<TextView>(R.id.stock_qty_left)

        val saveButton = addStockDialog.findViewById<Button>(R.id.add_stock_save_button)
        val editTextQty = addStockDialog.findViewById<TextView>(R.id.stock_edit_text_quantity)
        val plusButton = addStockDialog.findViewById<ImageView>(R.id.stock_qty_plus)
        val minusButton = addStockDialog.findViewById<ImageView>(R.id.stock_qty_minus)

        stockName?.text = inventoryItem.stockName
        stockPrice?.text = inventoryItem.sellingPrice.toString()
        stockQtyLeft?.text = inventoryItem.quantity.toString()

        plusButton?.setOnClickListener {
            try {
                var newQty = editTextQty?.text.toString().toInt()
                editTextQty?.setText((++newQty).toString())
            } catch (_: Exception) {
            }
        }
        minusButton?.setOnClickListener {
            try {
                if (editTextQty?.text.toString().toInt() > 0) {
                    var newQty = editTextQty?.text.toString().toInt()
                    editTextQty?.setText((--newQty).toString())
                }
            } catch (_: Exception) {
            }
        }

        saveButton?.setOnClickListener {
            try {
                val qty = editTextQty?.text.toString().toInt()
                if (qty > 0) {
                    inventoryItem.quantity = inventoryItem.quantity + qty
                    inventoryViewModel.updateStockItem(inventoryItem)
                    updateStockItem()
                    addStockDialog.dismiss()
                }
            } catch (_: Exception) {
                Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }

        addStockDialog.setCancelable(true)
        addStockDialog.show()
    }

    private fun updateStockItem() {
        lifecycleScope.launch {
            inventoryViewModel.updateInventoryItem().collect { result ->
                when (result) {
                    is Result.Success -> {
                        inventoryViewModel.getInventoryData()
                        Toast.makeText(requireContext(), "Entry updated!", Toast.LENGTH_LONG).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Error - please try again", Toast.LENGTH_LONG).show()
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }


}