package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.data.models.Customer
import com.chrisojukwu.tallybookkeeping.data.models.PaymentMode
import com.chrisojukwu.tallybookkeeping.data.models.Product
import com.chrisojukwu.tallybookkeeping.databinding.FragmentEditIncomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class EditIncomeFragment : Fragment() {
    private lateinit var binding: FragmentEditIncomeBinding
    private val vm: EditIncomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditIncomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = vm
        }

        (activity as AppCompatActivity?)!!.supportActionBar?.hide()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = EditIncomeProductListAdapter(
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

        binding.addDiscount.setOnClickListener {
            openDiscountBottomSheet()
        }

        binding.cardViewAddItems.setOnClickListener {
            openAddItemBottomSheet(null)
        }

        binding.cardViewAddMoreItemsButton.setOnClickListener {
            openAddItemBottomSheet(null)
        }

        binding.cardViewAddCustomer.setOnClickListener {
            openAddCustomerBottomSheet()
        }

        binding.cardViewRemoveCustomer.setOnClickListener {
            removeCustomerDetails()
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
            vm.clearDiscountFields()
            if (text!!.toString() == ".") {
                binding.editTextTotalAmount.setText("0.")
                binding.editTextAmountReceived.setText("0.")
                binding.editTextTotalAmount.setSelection(text.length + 1)
            } else if (text.isNotBlank()) {
                if (text.toString().toDouble() > 0.0) {
                    vm.updateTotalAmount(text.toString().toBigDecimal())
                    binding.editTextAmountReceived.setText(text)
                    binding.textViewAddDiscount.setTextColor(
                        requireContext().resources.getColor(
                            R.color.outline_blue,
                            null
                        )
                    )
                    binding.addDiscount.isClickable = true
                }
            } else {
                vm.updateTotalAmount(BigDecimal.ZERO)
                binding.editTextAmountReceived.setText(text)
                binding.textViewAddDiscount.setTextColor(requireContext().resources.getColor(R.color.grey, null))
                binding.addDiscount.isClickable = false
            }

        }
        binding.editTextAmountReceived.doOnTextChanged { text, _, _, _ ->
            if (text!!.toString() == ".") {
                binding.editTextAmountReceived.setText("0.")
                binding.editTextAmountReceived.setSelection(text.length + 1)
            } else if (text.isNotBlank()) {
                if (text.toString().toDouble() > 0.0) {
                    vm.setAmountReceived(text.toString().toBigDecimal())
                }
            } else {
                vm.setAmountReceived(BigDecimal.ZERO)
            }
        }

        binding.editTextDescription.doOnTextChanged { text, _, _, _ ->
            binding.buttonSave.isEnabled = text!!.isNotBlank()
        }

//        binding.editTextDescription.setOnFocusChangeListener { view, focus ->
//            if (focus) {
//                binding.editTextDescription.hint = ""
//            } else {binding.editTextDescription.hint = "2 boxes"}
//        }
    }

    private fun callObservers() {
        binding.addDiscount.isClickable = false

//        vm.subTotalAmount.observe(viewLifecycleOwner) {
//            vm.amountReceived.observe(viewLifecycleOwner) {
//                vm.updateBalanceDue()
//            }
//        }

        vm.discountAmount.observe(viewLifecycleOwner) {
            binding.editTextAmountReceived.setText(vm.updateAmountReceived())
        }

        vm.isDiscountAdded.observe(viewLifecycleOwner) { discountAdded ->
            if (discountAdded) {
                binding.apply {
                    textViewAddDiscount.visibility = View.GONE
                    openDiscountIcon.visibility = View.VISIBLE
                    layoutSubtotal.visibility = View.VISIBLE
                    layoutDiscountValues.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    openDiscountIcon.visibility = View.GONE
                    layoutDiscountValues.visibility = View.GONE
                    textViewAddDiscount.visibility = View.VISIBLE
                    layoutSubtotal.visibility = View.GONE

                }
            }
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

        vm.amountReceived.observe(viewLifecycleOwner) { amountReceived ->
            vm.subTotalAmount.observe(viewLifecycleOwner) { subTotal ->
                vm.updateBalanceDue()
                if (amountReceived != subTotal) {
                    binding.textViewCustomerTitle2.apply {
                        setText(R.string.required)
                        setTextColor(requireContext().resources.getColor(R.color.red, null))
                    }
                    vm.setCustomerRequirement(true)
                } else {
                    binding.textViewCustomerTitle2.apply {
                        setText(R.string.optional)
                        setTextColor(requireContext().resources.getColor(R.color.text_color3, null))
                    }
                    vm.setCustomerRequirement(false)
                }
            }
        }

        vm.isCustomerAdded.observe(viewLifecycleOwner) {
            if (it) {
                binding.cardViewAddCustomer.visibility = View.GONE
                binding.cardViewRemoveCustomer.visibility = View.VISIBLE
                binding.layoutCustomerDetails.visibility = View.VISIBLE
            } else {
                binding.cardViewRemoveCustomer.visibility = View.GONE
                binding.layoutCustomerDetails.visibility = View.GONE
                binding.cardViewAddCustomer.visibility = View.VISIBLE
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

    private fun openDiscountBottomSheet() {
        val discountBottomSheetDialog = BottomSheetDialog(requireContext())

        discountBottomSheetDialog.setContentView(R.layout.discount_bottomsheet)

        val mainLayout = discountBottomSheetDialog.findViewById<LinearLayout>(R.id.main_layout)
        val saveButton = discountBottomSheetDialog.findViewById<Button>(R.id.button_save_discount)
        val editTextDiscount = discountBottomSheetDialog.findViewById<EditText>(R.id.edit_text_discount)
        val textView = discountBottomSheetDialog.findViewById<TextView>(R.id.text_view_discount_type)
        val discountType = discountBottomSheetDialog.findViewById<RelativeLayout>(R.id.layout_discount_type)
        val removeButton = discountBottomSheetDialog.findViewById<TextView>(R.id.text_view_remove_discount)
        val closeIcon = discountBottomSheetDialog.findViewById<ImageView>(R.id.close_icon)

        mainLayout?.setOnFocusChangeListener { view, focus ->
            if (focus) {
                val imm =
                    requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        discountType?.setOnClickListener {
            val discountTypeBottomSheetDialog = BottomSheetDialog(requireContext())
            discountTypeBottomSheetDialog.setContentView(R.layout.discount_type_bottomsheet)

            val discountPercentLayout =
                discountTypeBottomSheetDialog.findViewById<RelativeLayout>(R.id.layout_discount_type_percent)
            val discountAmountLayout =
                discountTypeBottomSheetDialog.findViewById<RelativeLayout>(R.id.layout_discount_type_amount)
            val discountTypeCloseIcon =
                discountTypeBottomSheetDialog.findViewById<ImageView>(R.id.discount_type_close_icon_2)


            discountPercentLayout?.setOnClickListener {
                vm.updateDiscountUI(true)
                discountTypeBottomSheetDialog.dismiss()
            }

            discountAmountLayout?.setOnClickListener {
                vm.updateDiscountUI(false)
                discountTypeBottomSheetDialog.dismiss()
            }

            discountTypeCloseIcon?.setOnClickListener { discountTypeBottomSheetDialog.dismiss() }

            discountTypeBottomSheetDialog.setCancelable(true)
            discountTypeBottomSheetDialog.show()
        }

        removeButton?.setOnClickListener {
            vm.setDiscountAmount(0.00.toBigDecimal())
            vm.isDiscountAdded.value = false
            discountBottomSheetDialog.dismiss()
        }

        vm.isDiscountAdded.observe(viewLifecycleOwner) { discountAdded ->
            if (discountAdded) {
                removeButton?.visibility = View.VISIBLE
            } else {
                removeButton?.visibility = View.GONE
            }
        }

        saveButton?.setOnClickListener {
            if (vm.showPercent.value!!) {
                vm.saveDiscountType(true)
                vm.setDiscountPercentage(editTextDiscount?.text.toString().toDouble())
            } else {
                vm.saveDiscountType(false)
                vm.setDiscountAmount(editTextDiscount?.text.toString().toBigDecimal())
            }

            vm.isDiscountAdded.value = true
            discountBottomSheetDialog.dismiss()
        }

        editTextDiscount?.doOnTextChanged { text, _, _, _ ->
            if (text!!.toString() == ".") {
                editTextDiscount.setText("0.")
                editTextDiscount.setSelection(text.length + 1)
            } else if (text.isNotBlank()) {
                if (text.toString().toDouble() > 0) {
                    saveButton?.isEnabled = true
                }
            } else {
                saveButton?.isEnabled = false
            }
        }

        vm.showPercent.observe(viewLifecycleOwner) {
            if (it) {
                textView?.text = getString(R.string.percentage)
            } else {
                textView?.text = getString(R.string.discount_amount)
            }
        }

        closeIcon?.setOnClickListener { discountBottomSheetDialog.dismiss() }

        discountBottomSheetDialog.setCancelable(true)

        discountBottomSheetDialog.show()
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

    private fun openAddCustomerBottomSheet() {
        val addCustomerBottomSheetDialog = BottomSheetDialog(requireContext())

        addCustomerBottomSheetDialog.setContentView(R.layout.add_customer_bottomsheet)

        val doneButton = addCustomerBottomSheetDialog.findViewById<Button>(R.id.customer_button_done)
        val customerCloseButton = addCustomerBottomSheetDialog.findViewById<ImageView>(R.id.customer_close_icon)
        val customerName = addCustomerBottomSheetDialog.findViewById<EditText>(R.id.edit_text_customer_name)
        val customerPhone = addCustomerBottomSheetDialog.findViewById<EditText>(R.id.edit_text_customer_phone)

        customerName?.doOnTextChanged { text, _, _, _ ->
            doneButton?.isEnabled = text!!.isNotBlank() && customerPhone!!.text.isNotBlank()
        }

        customerPhone?.doOnTextChanged { text, _, _, _ ->
            doneButton?.isEnabled = text!!.isNotBlank() && customerName!!.text.isNotBlank()
        }

        doneButton?.setOnClickListener {
            vm.updateCustomerInfo(Customer(customerName?.text.toString(), customerPhone?.text.toString()))
            vm.customerAdded(true)
            addCustomerBottomSheetDialog.dismiss()
        }
        customerCloseButton?.setOnClickListener { addCustomerBottomSheetDialog.dismiss() }


        addCustomerBottomSheetDialog.setCancelable(true)
        addCustomerBottomSheetDialog.show()
    }

    private fun removeCustomerDetails() {
        vm.updateCustomerInfo(Customer("", ""))
        vm.customerAdded(false)
    }

    private fun onSaveButtonClicked() {
        when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radio_cash -> vm.setPaymentModeIncome(PaymentMode.CASH)
            R.id.radio_bank_transfer -> vm.setPaymentModeIncome(PaymentMode.BANK_TRANSFER)
            R.id.radio_pos -> vm.setPaymentModeIncome(PaymentMode.POS)
        }

        vm.updateDescription(binding.editTextDescription.text.toString())

        when {
            vm.totalAmount.value!! <= BigDecimal.ZERO -> showErrorSnackBar(R.string.total_amount_error)
            vm.amountReceived.value!! > vm.subTotalAmount.value -> showErrorSnackBar(R.string.amount_received_error)
            vm.discountAmount.value!! >= vm.totalAmount.value!! -> showErrorSnackBar(R.string.discount_error)
            vm.productList.value!!.size > 0 && vm.itemsTotalCost.value != vm.subTotalAmount.value &&
                    vm.isDiscountAdded.value!! ->
                showErrorSnackBar(R.string.items_total_error2)
            vm.productList.value!!.size > 0 && vm.itemsTotalCost.value != vm.subTotalAmount.value &&
                    !vm.isDiscountAdded.value!! ->
                showErrorSnackBar(R.string.items_total_error1)
            vm.isCustomerRequired.value!! && !vm.isCustomerAdded.value!! -> showErrorSnackBar(R.string.customer_error)
            else -> {
                if (vm.saveAllDetails()) {
                    Toast.makeText(requireContext(), "Entry saved!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_editIncomeFragment_to_homeFragment)
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