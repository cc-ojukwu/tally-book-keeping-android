package com.chrisojukwu.tallybookkeeping.ui.inventory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.databinding.FragmentStockItemDetailsBinding
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StockItemDetailsFragment : Fragment() {

    private lateinit var binding: FragmentStockItemDetailsBinding
    private val inventoryViewModel: InventoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStockItemDetailsBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            sharedViewModel = inventoryViewModel
        }

        requireActivity().window.statusBarColor = requireActivity().resources.getColor(R.color.background_color1, null)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.stockDetailsBackButton.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.stockDeleteButton.setOnClickListener {
            deleteStockBottomSheet()
        }

    }

    private fun deleteStockBottomSheet() {
        val deleteBottomSheetDialog = BottomSheetDialog(requireContext())

        deleteBottomSheetDialog.setContentView(R.layout.delete_inventory_bottomsheet)

        val deleteYes = deleteBottomSheetDialog.findViewById<TextView>(R.id.delete_stock_yes_button)
        val deleteNo = deleteBottomSheetDialog.findViewById<TextView>(R.id.delete_stock_no_button)

        deleteYes?.setOnClickListener {
            deleteInventory()
            deleteBottomSheetDialog.dismiss()
        }
        deleteNo?.setOnClickListener {
            deleteBottomSheetDialog.dismiss()
        }

        deleteBottomSheetDialog.setCancelable(true)
        deleteBottomSheetDialog.show()
    }

    private fun deleteInventory() {
        lifecycleScope.launch {
            inventoryViewModel.deleteInventory().collect { result ->
                when (result) {
                    is Result.Success -> {
                        inventoryViewModel.getInventoryData()
                        Toast.makeText(requireContext(), "Item deleted!", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_stockItemDetailsFragment_to_inventoryFragment)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Error, try again later", Toast.LENGTH_LONG).show()
                    }
                    is Result.Loading -> {}
                }

            }
        }
    }

}