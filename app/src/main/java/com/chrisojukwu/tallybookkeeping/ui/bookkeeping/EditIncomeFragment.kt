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
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.data.models.Customer
import com.chrisojukwu.tallybookkeeping.data.models.PaymentMode
import com.chrisojukwu.tallybookkeeping.data.models.Product
import com.chrisojukwu.tallybookkeeping.data.models.RecordHolder
import com.chrisojukwu.tallybookkeeping.databinding.FragmentEditIncomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@AndroidEntryPoint
class EditIncomeFragment : Fragment() {
    private lateinit var binding: FragmentEditIncomeBinding
    private val vm: EditIncomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditIncomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = vm
        }
        binding.productListRecyclerView.adapter = EditIncomeProductListAdapter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDateTime()

        binding.datePicker.setOnClickListener {
            openDateBottomSheet()
        }

        binding.addDiscount.setOnClickListener {
            openDiscountBottomSheet()
        }

        binding.cardViewAddItems.setOnClickListener {
            openAddItemBottomSheet()
        }

        binding.cardViewAddCustomer.setOnClickListener {
            openAddCustomerBottomSheet()
        }


        binding.buttonSave.setOnClickListener {
            onSaveButtonClicked()
        }

        onEditTextChangedCallback()

        callObservers()

    }

    private fun setDateTime() {
        val localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now())
        vm.saveDate(localDateTime)
    }


    private fun onEditTextChangedCallback() {
        binding.editTextTotalAmount.doOnTextChanged { text, _, _, _ ->
            vm.clearDiscountFields()
            if (text!!.toString().equals(".")) {
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
                vm.updateTotalAmount(0.toBigDecimal())
                binding.textViewAddDiscount.setTextColor(requireContext().resources.getColor(R.color.grey, null))
                binding.addDiscount.isClickable = false
            }

        }
        binding.editTextAmountReceived.doOnTextChanged { text, _, _, _ ->
            if (text!!.toString().equals(".")) {
                binding.editTextAmountReceived.setText("0.")
                binding.editTextAmountReceived.setSelection(text.length + 1)
            } else if (text.isNotBlank()) {
                if (text.toString().toDouble() > 0.0) {
                    vm.setAmountReceived(text.toString().toBigDecimal())
                }
            } else {
                vm.setAmountReceived(0.toBigDecimal())
            }
        }

        binding.editTextDescription.doOnTextChanged { text, _, _, _ ->
            binding.buttonSave.isEnabled = text!!.isNotBlank()
        }
    }

    private fun callObservers() {
        vm.subTotalAmount.observe(viewLifecycleOwner) { subtotalAmount ->
            vm.amountReceived.observe(viewLifecycleOwner) { amountReceived ->
                binding.textViewBalanceDue.text = (subtotalAmount - amountReceived).toString()
            }
        }

        vm.discountAmount.observe(viewLifecycleOwner) { discountAmount ->
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
                    textViewDiscountValues.visibility = View.GONE
                    textViewAddDiscount.visibility = View.VISIBLE
                    layoutSubtotal.visibility = View.GONE

                }
            }
        }

        vm.productList.observe(viewLifecycleOwner) { productList ->
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
                if (amountReceived != subTotal) {
                    binding.textViewCustomerTitle2.apply {
                        setText(R.string.required)
                        setTextColor(requireContext().resources.getColor(R.color.red, null))
                    }
                    vm.isCustomerRequired.value = true
                } else {
                    binding.textViewCustomerTitle2.apply {
                        setText(R.string.optional)
                        setTextColor(requireContext().resources.getColor(R.color.text_color3, null))
                    }
                    vm.isCustomerRequired.value = false
                }
            }
        }

        vm.isCustomerAdded.observe(viewLifecycleOwner) { isCustomerAdded ->
            if (isCustomerAdded) {
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
            val month = datePicker.month
            val year = datePicker.year

            val selectedDate = LocalDate.of(year, month, day)
            if (selectedDate.compareTo(LocalDate.now()) == 0) {
                val localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now())
                vm.saveDate(localDateTime)
            } else {
                val localDateTime = LocalDateTime.of(year, month, day, 0, 0)
                vm.saveDate(localDateTime)
            }

            dateBottomSheetDialog.dismiss()
        }

        val today = Calendar.getInstance()
        datePicker?.maxDate = today.timeInMillis
        today.time
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

            val selectPercent =
                discountTypeBottomSheetDialog.findViewById<RelativeLayout>(R.id.layout_discount_type_percent)
            val selectAmount =
                discountTypeBottomSheetDialog.findViewById<RelativeLayout>(R.id.layout_discount_type_amount)
            val discountTypeCloseIcon = discountTypeBottomSheetDialog.findViewById<ImageView>(R.id.close_icon2)


            selectPercent?.setOnClickListener {
                vm.setDiscountType(true)
                discountTypeBottomSheetDialog.dismiss()
            }

            selectAmount?.setOnClickListener {
                vm.setDiscountType(false)
                discountTypeBottomSheetDialog.dismiss()
            }

            removeButton?.setOnClickListener {
                vm.setDiscountAmount(0.00.toBigDecimal())
                vm.isDiscountAdded.value = false
                discountBottomSheetDialog.dismiss()
            }

            discountTypeCloseIcon?.setOnClickListener { discountTypeBottomSheetDialog.dismiss() }

            discountTypeBottomSheetDialog.setCancelable(true)
            discountTypeBottomSheetDialog.show()
        }

        saveButton?.setOnClickListener {
            vm.discountIsPercent.observe(viewLifecycleOwner) { discountIsPercent ->
                if (discountIsPercent) {
                    vm.updateDiscountPercentage(editTextDiscount?.text.toString().toDouble())
                } else {
                    vm.setDiscountAmount(editTextDiscount?.text.toString().toBigDecimal())
                }
            }
            vm.isDiscountAdded.value = true
            discountBottomSheetDialog.dismiss()
        }

        editTextDiscount?.doOnTextChanged { text, _, _, _ ->
            if (text!!.toString().equals(".")) {
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

        vm.discountIsPercent.observe(viewLifecycleOwner) {
            if (!it) {
                textView?.setText("Amount")
            } else {
                textView?.setText("Percentage(%)")
            }
        }

        closeIcon?.setOnClickListener { discountBottomSheetDialog.dismiss() }

        discountBottomSheetDialog.setCancelable(true)

        discountBottomSheetDialog.show()
    }


    private fun openAddItemBottomSheet() {
        val addItemsBottomSheetDialog = BottomSheetDialog(requireContext())

        addItemsBottomSheetDialog.setContentView(R.layout.add_item_bottomsheet)

        val product = Product("", 0.toBigDecimal(), 1)

        val itemCloseButton = addItemsBottomSheetDialog.findViewById<ImageView>(R.id.item_close_icon)
        val plusButton = addItemsBottomSheetDialog.findViewById<ImageView>(R.id.plus)
        val minusButton = addItemsBottomSheetDialog.findViewById<ImageView>(R.id.minus)
        val editTextQuantity = addItemsBottomSheetDialog.findViewById<EditText>(R.id.edit_text_quantity)
        val editTextProductName = addItemsBottomSheetDialog.findViewById<EditText>(R.id.edit_text_product_name)
        val editTextPrice = addItemsBottomSheetDialog.findViewById<EditText>(R.id.edit_text_price)
        val saveButton = addItemsBottomSheetDialog.findViewById<Button>(R.id.button_save_item)

        itemCloseButton?.setOnClickListener { addItemsBottomSheetDialog.dismiss() }

        plusButton?.setOnClickListener {
            product.productQuantity++
            editTextQuantity?.setText(product.productQuantity.toString())
        }
        minusButton?.setOnClickListener {
            if (product.productQuantity != 0) product.productQuantity--
            editTextQuantity?.setText(product.productQuantity.toString())
        }
        editTextQuantity?.doOnTextChanged { text, _, _, _ ->
            try {
                product.productQuantity = text.toString().toInt()
            } catch (e: Exception) {
            }
        }

        editTextProductName?.doOnTextChanged { text, _, _, _ ->
            editTextPrice?.setText("")
            if (text!!.isNotBlank()) {
                product.productName = text as String
            }
        }

        editTextPrice?.doOnTextChanged { text, _, _, _ ->
            try {
                if (text!!.toString().toDouble() > 0 && product.productName.isNotBlank()) {
                    product.productPrice = text.toString().toBigDecimal()
                    saveButton?.isEnabled = true
                }
            } catch (e: Exception) {
            }
        }


        saveButton?.setOnClickListener {
            try {
                product.productQuantity = editTextQuantity?.text.toString().toInt()
                if (product.productName.isNotBlank() && product.productPrice > BigDecimal.ZERO) {
                    vm.updateProductList(product)
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

        val doneButton = addCustomerBottomSheetDialog.findViewById<Button>(R.id.button_done)
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
            vm.updateCustomer(Customer(customerName?.text.toString(), customerPhone?.text.toString()))
            vm.isCustomerAdded.value = true
            addCustomerBottomSheetDialog.dismiss()
        }
        customerCloseButton?.setOnClickListener { addCustomerBottomSheetDialog.dismiss() }


        addCustomerBottomSheetDialog.setCancelable(true)
        addCustomerBottomSheetDialog.show()
    }

    private fun onSaveButtonClicked() {
        when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radio_cash -> vm.paymentMode = PaymentMode.CASH
            R.id.radio_bank_transfer -> vm.paymentMode = PaymentMode.BANK_TRANSFER
            R.id.radio_pos -> vm.paymentMode = PaymentMode.POS
        }

        vm.updateDescription(binding.editTextDescription.text.toString())

        when {
            vm.totalAmount.value!! == BigDecimal.ZERO -> showErrorSnackBar(ERROR1)
            vm.amountReceived.value!! > vm.subTotalAmount.value -> showErrorSnackBar(ERROR2)
            vm.discountAmount.value!! >= vm.totalAmount.value!! -> showErrorSnackBar(ERROR3)
            vm.productList.value!!.size > 0 && vm.itemsTotalCost.value != vm.subTotalAmount.value -> showErrorSnackBar(
                ERROR4
            )
            vm.isCustomerRequired.value!! && !vm.isCustomerAdded.value!! -> showErrorSnackBar(ERROR5)
            else -> {
                if (vm.saveAllDetails()) {
                    Toast.makeText(requireContext(), "Entry saved!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_editIncomeFragment_to_homeFragment)
                }
            }
        }
    }

    private fun showErrorSnackBar(error_text: Int) {
        Snackbar.make(binding.buttonSave, error_text, Snackbar.LENGTH_LONG)
            .setTextColor(Color.WHITE)
            .setBackgroundTint(Color.RED)
            .show()
    }

}

const val ERROR1 = R.string.total_amount_error
const val ERROR2 = R.string.amount_received_error
const val ERROR3 = R.string.discount_error
const val ERROR4 = R.string.items_total_error
const val ERROR5 = R.string.customer_error