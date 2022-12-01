package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chrisojukwu.tallybookkeeping.data.models.Customer
import com.chrisojukwu.tallybookkeeping.data.models.PaymentMode
import com.chrisojukwu.tallybookkeeping.data.models.Product
import com.chrisojukwu.tallybookkeeping.data.models.RecordHolder
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class EditIncomeViewModel @Inject constructor() : ViewModel() {


    private var _transactionDate = LocalDateTime.now()
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val transactionDate = MutableLiveData("${_transactionDate.format(formatter)}")

    private val _discountIsPercent = MutableLiveData<Boolean>(true)
    val discountIsPercent: LiveData<Boolean> = _discountIsPercent

    private val _discountAmount = MutableLiveData<BigDecimal>(0.00.toBigDecimal())
    val discountAmount: LiveData<BigDecimal> = _discountAmount

    private val _totalAmount = MutableLiveData<BigDecimal>(0.00.toBigDecimal())
    val totalAmount: LiveData<BigDecimal> = _totalAmount

    private val _subTotalAmount =
        MutableLiveData<BigDecimal>(_totalAmount.value!!.minus(_discountAmount.value!!))
    val subTotalAmount: LiveData<BigDecimal> = _subTotalAmount

    private val _discountPercentage = MutableLiveData<Double>(0.00)
    val discountPercentage: LiveData<Double> = _discountPercentage

    private val _amountReceived = MutableLiveData<BigDecimal>(0.00.toBigDecimal())
    val amountReceived: LiveData<BigDecimal> = _amountReceived

    private val _balanceDue =
        MutableLiveData<BigDecimal>(_subTotalAmount.value!! - _amountReceived.value!!)
    val balanceDue: LiveData<BigDecimal> = _balanceDue

    val isDiscountAdded = MutableLiveData<Boolean>(false)

    private val _productList = MutableLiveData<MutableList<Product>>(mutableListOf())
    val productList: LiveData<MutableList<Product>> = _productList

    val productCount = MutableLiveData<Int>(_productList.value?.size)

    private val _itemsTotalCost = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val itemsTotalCost: LiveData<BigDecimal> = _itemsTotalCost

    val isCustomerRequired = MutableLiveData<Boolean>(false)

    val isCustomerAdded = MutableLiveData<Boolean>(false)

    private val _customer = MutableLiveData<Customer>()
    val customer: LiveData<Customer> = _customer

    private val _description = MutableLiveData<String>("")

    var paymentMode: PaymentMode = PaymentMode.CASH


    fun saveDate(date: LocalDateTime) {
        _transactionDate = date
    }

    fun setDiscountType(isPercent: Boolean) {
        _discountIsPercent.value = isPercent
    }

    fun setDiscountAmount(amount: BigDecimal) {
        _discountAmount.value = amount
        _discountPercentage.value = (amount / _totalAmount.value!!).toDouble() * 100
        updateSubtotal()
    }

    fun clearDiscountFields() {
        _discountAmount.value = BigDecimal.ZERO
        _discountPercentage.value = 0.00
        isDiscountAdded.value = false
    }

    fun updateDiscountPercentage(percent: Double) {
        _discountPercentage.value = percent
        _discountAmount.value = (percent / 100).toBigDecimal() * _totalAmount.value!!
        updateSubtotal()
    }

    fun updateTotalAmount(totalAmount: BigDecimal) {
        _totalAmount.value = totalAmount
        updateSubtotal()
    }

    fun setAmountReceived(amountReceived: BigDecimal) {
        _amountReceived.value = amountReceived
    }

    fun updateAmountReceived(): String {
        _amountReceived.value = _totalAmount.value!! - _discountAmount.value!!
        return _amountReceived.value.toString()
    }

    fun updateProductList(product: Product) {
        productList.value?.add(product)
        updateItemsTotalCost()
    }

    fun updateCustomer(customer: Customer) {
        _customer.value = customer
    }

    fun updateSubtotal() {
        _subTotalAmount.value = _totalAmount.value!! - _discountAmount.value!!
        updateBalanceDue()
    }

    fun updateBalanceDue() {
        _balanceDue.value = _subTotalAmount.value!! - _amountReceived.value!!
    }

    fun updateItemsTotalCost() {
        _itemsTotalCost.value = _productList.value?.sumOf { it.productTotalPrice }
    }

    fun updateDescription(desc: String) {
        _description.value = desc
    }

    fun saveAllDetails() : Boolean {
        val income = RecordHolder.Income(
            date = _transactionDate,
            totalAmount = _totalAmount.value!!,
            amountReceived = _amountReceived.value!!,
            discount = _discountAmount.value!!,
            subTotal = _subTotalAmount.value!!,
            balanceDue = _balanceDue.value!!,
            description = _description.value!!,
            productList = _productList.value,
            paymentMode = paymentMode
        )
        return true
    }

}