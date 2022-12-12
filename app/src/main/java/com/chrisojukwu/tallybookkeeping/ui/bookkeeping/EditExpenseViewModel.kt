package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chrisojukwu.tallybookkeeping.data.models.*
import com.chrisojukwu.tallybookkeeping.utils.formatDateToString
import com.chrisojukwu.tallybookkeeping.utils.notifyObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EditExpenseViewModel @Inject constructor() : ViewModel() {

    private var _transactionDate = MutableLiveData(LocalDateTime.now())
    val transactionDate: LiveData<String> =
        Transformations.switchMap(_transactionDate) { date -> formatDateToString(date) }

    private val _totalAmount = MutableLiveData(BigDecimal.ZERO)
    val totalAmount: LiveData<BigDecimal> = _totalAmount

    private val _amountPaid = MutableLiveData(BigDecimal.ZERO)
    val amountPaid: LiveData<BigDecimal> = _amountPaid

    private val _balanceDue = MutableLiveData(BigDecimal.ZERO)
    val balanceDue: LiveData<BigDecimal> = _balanceDue

    private val _productList = MutableLiveData<MutableList<Product>>(mutableListOf())
    val productList: LiveData<MutableList<Product>> = _productList

    private val _productCount = Transformations.map(_productList) { list -> list.size }
    val productCount = _productCount

    private val _itemsTotalCost = MutableLiveData(BigDecimal.ZERO)
    val itemsTotalCost: LiveData<BigDecimal> = _itemsTotalCost

    private val _description = MutableLiveData("")

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> = _category

    private val _isCategorySelected = MutableLiveData(false)
    val isCategorySelected: LiveData<Boolean> = _isCategorySelected

    private val _supplierInfo = MutableLiveData<Supplier>()
    val supplierInfo: LiveData<Supplier> = _supplierInfo

    private val _isSupplierRequired = MutableLiveData(false)
    val isSupplierRequired: LiveData<Boolean> = _isSupplierRequired

    private val _isSupplierAdded = MutableLiveData(false)
    val isSupplierAdded: LiveData<Boolean> = _isSupplierAdded

    private var _paymentMode: PaymentMode = PaymentMode.CASH

    fun updateProductList(product: Product) {
        _productList.value?.filter { it.id == product.id }?.forEach {
            it.productName = product.productName
            it.productPrice = product.productPrice
            it.productQuantity = product.productQuantity
        }
        _productList.notifyObserver()
        updateItemsTotalCost()
    }

    fun addToProductList(productItem: Product) {
        _productList.value?.add(productItem)
        _productList.notifyObserver()
        updateItemsTotalCost()
    }

    fun removeFromProductList(productItem: Product) {
        _productList.value?.remove(productItem)
        _productList.notifyObserver()
        updateItemsTotalCost()
    }

    private fun updateItemsTotalCost() {
        _itemsTotalCost.value = _productList.value?.sumOf { it.productTotalPrice }
    }

    fun saveDate(date: LocalDateTime) {
        _transactionDate.value = date
    }

    fun updateTotalAmount(totalAmount: BigDecimal) {
        _totalAmount.value = totalAmount
    }

    fun setAmountPaid(amountPaid: BigDecimal) {
        _amountPaid.value = amountPaid
    }

    fun updateBalanceDue() {
        _balanceDue.value = _totalAmount.value!! - _amountPaid.value!!
    }

    fun updateDescription(desc: String) {
        _description.value = desc
    }

    fun updateCategory(category: String) {
        _category.value = category
    }

    fun categorySelected(value: Boolean) {
        _isCategorySelected.value = value
    }

    fun setSupplierRequirement(isRequired: Boolean) {
        _isSupplierRequired.value = isRequired
    }

    fun supplierAdded(supplierAdded: Boolean) {
        _isSupplierAdded.value = supplierAdded
    }

    fun updateSupplierInfo(supplier: Supplier) {
        _supplierInfo.value = supplier
    }

    fun setPaymentModeExpense(mode: PaymentMode) {
        _paymentMode = mode
    }

    fun saveAllDetails(): Boolean {
        val expense = RecordHolder.Expense(
            recordId = "${(0..50).random()}${(0..50).random()}${(0..50).random()}${(0..50).random()}",
            date = _transactionDate.value!!,
            totalAmount = _totalAmount.value!!,
            amountPaid = _amountPaid.value!!,
            balanceDue = _balanceDue.value!!,
            description = _description.value!!,
            category = _category.value,
            productList = _productList.value,
            paymentList = mutableListOf(
                Payment(_amountPaid.value!!, _transactionDate.value!!, _paymentMode)
            )
        )

        return true
    }
}