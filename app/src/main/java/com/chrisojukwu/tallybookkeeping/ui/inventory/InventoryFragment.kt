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
import com.chrisojukwu.tallybookkeeping.domain.model.StockItem
import com.chrisojukwu.tallybookkeeping.databinding.FragmentInventoryBinding
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InventoryFragment : Fragment() {

    private lateinit var binding: FragmentInventoryBinding
    private val inventoryViewModel: InventoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentInventoryBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = inventoryViewModel
        }

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
                .stockItemList = list
        }

    }

    private fun navigateToStockDetails(stockItem: StockItem) {
        inventoryViewModel.setStockDetails(stockItem)
        findNavController().navigate(R.id.action_inventoryFragment_to_stockItemDetailsFragment)
    }

    private fun navigateToStockEditPage(stockItem: StockItem) {
        inventoryViewModel.setEditStockDetails(stockItem)
        findNavController().navigate(R.id.action_inventoryFragment_to_editInventoryItemFragment)
    }

    private fun openAddStockBottomSheet(stockItem: StockItem) {
        val addStockDialog = BottomSheetDialog(requireContext())

        addStockDialog.setContentView(R.layout.add_stock_bottomsheet)

        val stockName = addStockDialog.findViewById<TextView>(R.id.stock_item_name)
        val stockPrice = addStockDialog.findViewById<TextView>(R.id.stock_item_selling_price)
        val stockQtyLeft = addStockDialog.findViewById<TextView>(R.id.stock_qty_left)

        val saveButton = addStockDialog.findViewById<Button>(R.id.add_stock_save_button)
        val editTextQty = addStockDialog.findViewById<TextView>(R.id.stock_edit_text_quantity)
        val plusButton = addStockDialog.findViewById<ImageView>(R.id.stock_qty_plus)
        val minusButton = addStockDialog.findViewById<ImageView>(R.id.stock_qty_minus)

        stockName?.text = stockItem.stockName
        stockPrice?.text = stockItem.sellingPrice.toString()
        stockQtyLeft?.text = stockItem.quantity.toString()

        plusButton?.setOnClickListener {
            try {
                var newQty = editTextQty?.text.toString().toInt()
                editTextQty?.setText(++newQty)
            } catch (_: Exception) {
            }
        }
        minusButton?.setOnClickListener {
            try {
                if (editTextQty?.text.toString().toInt() > 0) {
                    var newQty = editTextQty?.text.toString().toInt()
                    editTextQty?.setText(--newQty)
                }
            } catch (_: Exception) {
            }
        }

        saveButton?.setOnClickListener {
            try {
                val qty = editTextQty?.text.toString().toInt()
                if (qty > 0) {
                    stockItem.quantity = stockItem.quantity + qty
                    inventoryViewModel.updateStockItem(stockItem)
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
        lifecycleScope.launch(Dispatchers.IO) {
            inventoryViewModel.updateInventoryItem().collect { result ->
                when (result) {
                    is Result.Success -> {
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