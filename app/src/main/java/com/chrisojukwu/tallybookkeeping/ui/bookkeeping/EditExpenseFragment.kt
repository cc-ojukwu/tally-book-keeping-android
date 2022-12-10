package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.data.models.Customer
import com.chrisojukwu.tallybookkeeping.data.models.PaymentMode
import com.chrisojukwu.tallybookkeeping.data.models.Product
import com.chrisojukwu.tallybookkeeping.data.models.Supplier
import com.chrisojukwu.tallybookkeeping.databinding.FragmentEditExpenseBinding
import com.chrisojukwu.tallybookkeeping.utils.setupMaxHeight
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class EditExpenseFragment : Fragment() {

    private lateinit var binding: FragmentEditExpenseBinding
    private val vm: EditExpenseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditExpenseBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = vm
        }

        (activity as AppCompatActivity?)!!.supportActionBar?.hide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = EditExpenseProductListAdapter(
            mutableListOf(),
            { productItem -> vm.removeFromProductList(productItem) },
            { productItem -> openAddItemBottomSheet(productItem) })

        binding.productListRecyclerView.adapter = adapter

        setDateTime()

        binding.imageViewBackButton.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        binding.datePicker.setOnClickListener {
            openDateBottomSheet()
        }

        binding.cardViewAddItems.setOnClickListener {
            openAddItemBottomSheet(null)
        }

        binding.cardViewAddMoreItemsButton.setOnClickListener {
            openAddItemBottomSheet(null)
        }

        binding.layoutCategory.setOnClickListener {
            openCategoryBottomSheet()
        }

        binding.removeCategoryIcon.setOnClickListener {
            removeCategory()
        }

        binding.cardViewAddSupplier.setOnClickListener {
            openAddSupplierBottomSheet()
        }

        binding.cardViewRemoveSupplier.setOnClickListener {
            removeSupplierDetails()
        }

        binding.buttonSave.setOnClickListener {
            onSaveButtonClicked()
        }

        onEditTextChangedCallback()

        callObservers()
    }

    private fun setDateTime() {
        vm.saveDate(LocalDateTime.now())
    }

    private fun onEditTextChangedCallback() {
        binding.editTextTotalAmount.doOnTextChanged { text, _, _, _ ->
            if (text!!.toString().equals(".")) {
                binding.editTextTotalAmount.setText("0.")
                binding.editTextAmountPaid.setText("0.")
                binding.editTextTotalAmount.setSelection(text.length + 1)
            } else if (text.isNotBlank()) {
                if (text.toString().toDouble() > 0.0) {
                    vm.updateTotalAmount(text.toString().toBigDecimal())
                    binding.editTextAmountPaid.setText(text)
                }
            } else {
                vm.updateTotalAmount(BigDecimal.ZERO)
                binding.editTextAmountPaid.setText(text)
            }
        }

        binding.editTextAmountPaid.doOnTextChanged { text, _, _, _ ->
            if (text!!.toString().equals(".")) {
                binding.editTextAmountPaid.setText("0.")
                binding.editTextAmountPaid.setSelection(text.length + 1)
            } else if (text.isNotBlank()) {
                if (text.toString().toDouble() > 0.0) {
                    vm.setAmountPaid(text.toString().toBigDecimal())
                }
            } else {
                vm.setAmountPaid(BigDecimal.ZERO)
            }
        }

        binding.editTextDescription.doOnTextChanged { text, _, _, _ ->
            binding.buttonSave.isEnabled = text!!.isNotBlank()
        }
    }

    private fun callObservers() {
        vm.amountPaid.observe(viewLifecycleOwner) {
            vm.updateBalanceDue()
        }

        vm.productList.observe(viewLifecycleOwner) { productList ->
            binding.editTextDescription.setText(
                productList.joinToString(", ") {
                    "${it.productQuantity} ${it.productName}"
                }
            )
            if (productList.size > 0) {
                binding.cardViewAddItems.visibility = View.GONE
                binding.cardViewAddedItems.visibility = View.VISIBLE

            } else {
                binding.cardViewAddItems.visibility = View.VISIBLE
                binding.cardViewAddedItems.visibility = View.GONE
            }
        }

        vm.balanceDue.observe(viewLifecycleOwner) {
            if (it != BigDecimal.ZERO) {
                binding.textViewSupplierTitle2.apply {
                    setText(R.string.required)
                    setTextColor(requireContext().resources.getColor(R.color.red, null))
                }
                vm.setSupplierRequirement(true)
            } else {
                binding.textViewSupplierTitle2.apply {
                    setText(R.string.optional)
                    setTextColor(requireContext().resources.getColor(R.color.text_color3, null))
                }
                vm.setSupplierRequirement(false)
            }
        }

        vm.isCategorySelected.observe(viewLifecycleOwner) {
            if (it) {
                binding.openCategoryIcon.visibility = View.GONE
                binding.removeCategoryIcon.visibility = View.VISIBLE
            } else {
                binding.openCategoryIcon.visibility = View.VISIBLE
                binding.removeCategoryIcon.visibility = View.GONE
            }
        }

        vm.isSupplierAdded.observe(viewLifecycleOwner) {
            if (it) {
                binding.cardViewAddSupplier.visibility = View.GONE
                binding.cardViewRemoveSupplier.visibility = View.VISIBLE
                binding.layoutSupplierDetails.visibility = View.VISIBLE
            } else {
                binding.cardViewRemoveSupplier.visibility = View.GONE
                binding.layoutSupplierDetails.visibility = View.GONE
                binding.cardViewAddSupplier.visibility = View.VISIBLE
            }
        }
    }

    private fun openDateBottomSheet() {
        val dateBottomSheetDialog = BottomSheetDialog(requireContext())
        dateBottomSheetDialog.setContentView(R.layout.date_picker_bottomsheet)

        val datePicker = dateBottomSheetDialog.findViewById<DatePicker>(R.id.date_picker)

        val datePickerDone = dateBottomSheetDialog.findViewById<TextView>(R.id.date_picker_done)

        datePickerDone?.setOnClickListener {
            val day = datePicker!!.dayOfMonth
            val month = datePicker.month + 1
            val year = datePicker.year

            val selectedDate = LocalDate.of(year, month, day)
            if (selectedDate.compareTo(LocalDate.now()) == 0) {
                vm.saveDate(LocalDateTime.now())
            } else {
                val transactionDate = LocalDateTime.of(year, month, day, 0, 0)
                vm.saveDate(transactionDate)
            }

            dateBottomSheetDialog.dismiss()
        }

        val today = Calendar.getInstance()
        datePicker?.maxDate = today.timeInMillis
        val pattern = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = LocalDate.parse(vm.transactionDate.value, pattern)
        datePicker?.updateDate(date.year, date.monthValue - 1, date.dayOfMonth)
        dateBottomSheetDialog.setCancelable(true)
        dateBottomSheetDialog.show()
    }

    private fun openAddItemBottomSheet(productItem: Product?) {
        val addItemsBottomSheetDialog = BottomSheetDialog(requireContext())

        addItemsBottomSheetDialog.setContentView(R.layout.add_item_bottomsheet)

        val product = Product("${(0..20).random()}${(0..20).random()}${(0..20).random()}")

        val itemCloseButton = addItemsBottomSheetDialog.findViewById<ImageView>(R.id.item_close_icon)
        val plusButton = addItemsBottomSheetDialog.findViewById<ImageView>(R.id.plus)
        val minusButton = addItemsBottomSheetDialog.findViewById<ImageView>(R.id.minus)
        val editTextQuantity = addItemsBottomSheetDialog.findViewById<EditText>(R.id.edit_text_quantity)
        val editTextProductName = addItemsBottomSheetDialog.findViewById<EditText>(R.id.edit_text_product_name)
        val editTextPrice = addItemsBottomSheetDialog.findViewById<EditText>(R.id.edit_text_price)
        val saveButton = addItemsBottomSheetDialog.findViewById<Button>(R.id.button_save_item)

        if (productItem != null) {
            editTextProductName?.setText(productItem.productName)
            editTextPrice?.setText(productItem.productPrice.toString())
            editTextQuantity?.setText(productItem.productQuantity.toString())
        }

        itemCloseButton?.setOnClickListener { addItemsBottomSheetDialog.dismiss() }

        plusButton?.setOnClickListener {
            product.productQuantity++
            editTextQuantity?.setText(product.productQuantity.toString())
        }
        minusButton?.setOnClickListener {
            if (product.productQuantity != 0) product.productQuantity--
            editTextQuantity?.setText(product.productQuantity.toString())
        }

        editTextProductName?.doOnTextChanged { text, _, _, _ ->
            editTextPrice?.setText("")
            if (text!!.isNotBlank()) {
                product.productName = text.toString()
            }
        }

        editTextPrice?.doOnTextChanged { text, _, _, _ ->
            try {
                if (text!!.toString().toDouble() > 0 && editTextProductName!!.text.isNotBlank()) {
                    product.productPrice = text.toString().toBigDecimal()
                    saveButton?.isEnabled = true
                } else {
                    saveButton?.isEnabled = false
                }
            } catch (e: Exception) {
                saveButton?.isEnabled = false
            }
        }

        editTextQuantity?.doOnTextChanged { text, _, _, _ ->
            try {
                product.productQuantity = text.toString().toInt()
                saveButton?.isEnabled = editTextProductName!!.text.isNotBlank() &&
                        (editTextPrice!!.text.toString().toDouble() > 0) &&
                        editTextQuantity.text.toString().toInt() > 0
            } catch (e: Exception) {
            }
        }

        saveButton?.setOnClickListener {
            try {
                if (productItem != null) {
                    productItem.productName = editTextProductName?.text.toString()
                    productItem.productPrice = editTextPrice?.text.toString().toBigDecimal()
                    productItem.productQuantity = editTextQuantity?.text.toString().toInt()
                    productItem.productTotalPrice =
                        productItem.productPrice.multiply(productItem.productQuantity.toBigDecimal())
                    vm.updateProductList(productItem)
                    addItemsBottomSheetDialog.dismiss()
                }
                product.productQuantity = editTextQuantity?.text.toString().toInt()
                if (product.productName.isNotBlank() &&
                    (product.productPrice > BigDecimal.ZERO) &&
                    product.productQuantity > 0
                ) {
                    product.productTotalPrice =
                        product.productPrice.multiply(product.productQuantity.toBigDecimal())

                    vm.addToProductList(product)

                    addItemsBottomSheetDialog.dismiss()
                }
            } catch (e: Exception) {
            }
        }

        addItemsBottomSheetDialog.setCancelable(true)
        addItemsBottomSheetDialog.show()
    }

    private fun openCategoryBottomSheet() {
        val addCategoryBottomSheetDialog = BottomSheetDialog(requireContext())

        addCategoryBottomSheetDialog.setContentView(R.layout.expense_categories_bottomsheet)
        val parentLayout =
            addCategoryBottomSheetDialog.findViewById<View>(R.id.parent_layout)
        parentLayout?.setupMaxHeight(0.7)

        val recyclerView = addCategoryBottomSheetDialog.findViewById<RecyclerView>(R.id.category_recycler_view)
        val categoriesAdapter = ExpenseCategoryAdapter { item ->
            binding.textViewCategory.text = item
            vm.categorySelected(true)
            vm.updateCategory(item)
            addCategoryBottomSheetDialog.dismiss()
        }
        recyclerView?.adapter = categoriesAdapter

        val searchView = addCategoryBottomSheetDialog.findViewById<SearchView>(R.id.search_bar)

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val filteredList = mutableListOf<String>()
                for (item in expenseCategoryList) {
                    if (item.lowercase(Locale.ROOT).contains(newText.lowercase(Locale.ROOT))) {
                        filteredList.add(item)
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(requireContext(), "No match found...", Toast.LENGTH_SHORT).show()
                } else {
                    categoriesAdapter.filterList(filteredList)
                }
                return false
            }
        })

        addCategoryBottomSheetDialog.setCancelable(true)
        addCategoryBottomSheetDialog.show()
    }

    private fun removeCategory() {
        binding.textViewCategory.text = "Select Category"
        vm.updateCategory("")
        vm.categorySelected(false)
    }

    private fun openAddSupplierBottomSheet() {
        val addSupplierBottomSheetDialog = BottomSheetDialog(requireContext())

        addSupplierBottomSheetDialog.setContentView(R.layout.add_supplier_bottomsheet)

        val doneButton = addSupplierBottomSheetDialog.findViewById<Button>(R.id.supplier_button_done)
        val supplierCloseButton = addSupplierBottomSheetDialog.findViewById<ImageView>(R.id.supplier_close_icon)
        val supplierName = addSupplierBottomSheetDialog.findViewById<EditText>(R.id.edit_text_supplier_name)
        val supplierPhone = addSupplierBottomSheetDialog.findViewById<EditText>(R.id.edit_text_supplier_phone)

        supplierName?.doOnTextChanged { text, _, _, _ ->
            doneButton?.isEnabled = text!!.isNotBlank() && supplierPhone!!.text.isNotBlank()
        }

        supplierPhone?.doOnTextChanged { text, _, _, _ ->
            doneButton?.isEnabled = text!!.isNotBlank() && supplierName!!.text.isNotBlank()
        }

        doneButton?.setOnClickListener {
            vm.updateSupplierInfo(Supplier(supplierName?.text.toString(), supplierPhone?.text.toString()))
            vm.supplierAdded(true)
            addSupplierBottomSheetDialog.dismiss()
        }
        supplierCloseButton?.setOnClickListener { addSupplierBottomSheetDialog.dismiss() }


        addSupplierBottomSheetDialog.setCancelable(true)
        addSupplierBottomSheetDialog.show()
    }

    private fun removeSupplierDetails() {
        vm.updateSupplierInfo(Supplier("", ""))
        vm.supplierAdded(false)
    }

    private fun onSaveButtonClicked() {
        when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radio_cash -> vm.setPaymentModeExpense(PaymentMode.CASH)
            R.id.radio_bank_transfer -> vm.setPaymentModeExpense(PaymentMode.BANK_TRANSFER)
            R.id.radio_pos -> vm.setPaymentModeExpense(PaymentMode.POS)
        }

        vm.updateDescription(binding.editTextDescription.text.toString())

        when {
            vm.totalAmount.value!! <= BigDecimal.ZERO -> showErrorSnackBar(R.string.total_amount_error)
            vm.amountPaid.value!! > vm.totalAmount.value -> showErrorSnackBar(R.string.amount_received_error)
            vm.productList.value!!.size > 0 && vm.itemsTotalCost.value != vm.totalAmount.value ->
                showErrorSnackBar(R.string.items_total_error1)
            vm.isSupplierRequired.value!! && !vm.isSupplierAdded.value!! -> showErrorSnackBar(R.string.supplier_error)
            else -> {
                if (vm.saveAllDetails()) {
                    Toast.makeText(requireContext(), "Entry saved!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_editExpenseFragment_to_homeFragment)
                }
            }
        }
    }

    private fun showErrorSnackBar(error_text: Int) {
        Snackbar.make(binding.buttonSave, error_text, 4000)
            .setTextColor(Color.BLACK)
            .setBackgroundTint(requireContext().resources.getColor(R.color.expense_button_red, null))
            .show()
    }
}

