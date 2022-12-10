package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.*
import com.chrisojukwu.tallybookkeeping.data.models.Customer
import com.chrisojukwu.tallybookkeeping.data.models.PaymentMode
import com.chrisojukwu.tallybookkeeping.data.models.Product
import com.chrisojukwu.tallybookkeeping.data.models.RecordHolder
import com.chrisojukwu.tallybookkeeping.utils.formatDateToString
import com.chrisojukwu.tallybookkeeping.utils.notifyObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EditIncomeViewModel @Inject constructor() : ViewModel() {


    private var _transactionDate = MutableLiveData<LocalDateTime>(LocalDateTime.now())
    val transactionDate: LiveData<String> =
        Transformations.switchMap(_transactionDate) { date -> formatDateToString(date) }

    private val _discountIsPercent = MutableLiveData<Boolean>(true)
    val discountIsPercent: LiveData<Boolean> = _discountIsPercent

    private val _showPercent = MutableLiveData<Boolean>(true)
    val showPercent: LiveData<Boolean> = _showPercent

    private val _discountAmount = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val discountAmount: LiveData<BigDecimal> = _discountAmount

    private val _discountPercentage = MutableLiveData<Double>(0.0)
    val discountPercentage: LiveData<Double> = _discountPercentage

    private val _totalAmount = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val totalAmount: LiveData<BigDecimal> = _totalAmount

    private val _subTotalAmount =
        MutableLiveData<BigDecimal>(_totalAmount.value!!.minus(_discountAmount.value!!))
    val subTotalAmount: LiveData<BigDecimal> = _subTotalAmount

    private val _amountReceived = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val amountReceived: LiveData<BigDecimal> = _amountReceived

    private val _balanceDue = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val balanceDue: LiveData<BigDecimal> = _balanceDue

    val isDiscountAdded = MutableLiveData<Boolean>(false)

    private val _productList = MutableLiveData<MutableList<Product>>(mutableListOf())
    val productList: LiveData<MutableList<Product>> = _productList

    val _productCount = Transformations.map(_productList) { list -> list.size }
    val productCount = _productCount

    private val _itemsTotalCost = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val itemsTotalCost: LiveData<BigDecimal> = _itemsTotalCost

    private val _isCustomerRequired = MutableLiveData<Boolean>(false)
    val isCustomerRequired: LiveData<Boolean> = _isCustomerRequired

    private val _isCustomerAdded = MutableLiveData<Boolean>(false)
    val isCustomerAdded: LiveData<Boolean> = _isCustomerAdded

    private val _customerInfo = MutableLiveData<Customer>()
    val customerInfo: LiveData<Customer> = _customerInfo

    private val _description = MutableLiveData<String>("")

    var paymentMode: PaymentMode = PaymentMode.CASH


    fun saveDate(date: LocalDateTime) {
        _transactionDate.value = date
    }

    fun updateDiscountUI(isPercent: Boolean) {
        _showPercent.value = isPercent
    }

    fun saveDiscountType(isPercent: Boolean) {
        _discountIsPercent.value = isPercent
    }

    fun clearDiscountFields() {
        _discountAmount.value = BigDecimal.ZERO
        _discountPercentage.value = 0.0
        isDiscountAdded.value = false
    }

    fun setDiscountPercentage(percent: Double) {
        _discountPercentage.value = percent
        _discountAmount.value = (percent / 100).toBigDecimal(MathContext(16, RoundingMode.DOWN)) * _totalAmount.value!!
        updateSubtotal()
    }

    fun setDiscountAmount(amount: BigDecimal) {
        _discountAmount.value = amount
        val value = amount.divide(_totalAmount.value!!, 4, RoundingMode.DOWN)
        _discountPercentage.value = value.toDouble() * 100.0
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

    fun addToProductList(product: Product) {
        _productList.value?.add(product)
        _productList.notifyObserver()
        updateItemsTotalCost()
    }

    fun removeFromProductList(product: Product) {
        _productList.value?.remove(product)
        _productList.notifyObserver()
        updateItemsTotalCost()
    }

    fun updateProductList(product: Product) {
        _productList.value?.filter { it.id == product.id }?.forEach {
            it.productName = product.productName
            it.productPrice = product.productPrice
            it.productQuantity = product.productQuantity
        }
        _productList.notifyObserver()
        updateItemsTotalCost()
    }

    fun updateItemsTotalCost() {
        _itemsTotalCost.value = _productList.value?.sumOf { it.productTotalPrice }
    }

    fun updateCustomerInfo(customer: Customer) {
        _customerInfo.value = customer
    }

    fun updateSubtotal() {
        _subTotalAmount.value = _totalAmount.value!! - _discountAmount.value!!
        updateBalanceDue()
    }

    fun updateBalanceDue() {
        _balanceDue.value = _subTotalAmount.value!! - _amountReceived.value!!
    }

    fun updateDescription(desc: String) {
        _description.value = desc
    }

    fun saveAllDetails(): Boolean {
        val income = RecordHolder.Income(
            recordId = "${(0..50).random()}${(0..50).random()}${(0..50).random()}${(0..50).random()}",
            date = _transactionDate.value!!,
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

    fun setCustomerRequirement(isCustomerRequired: Boolean) {
        _isCustomerRequired.value = isCustomerRequired
    }

    fun customerAdded(customerAdded: Boolean) {
        _isCustomerAdded.value = customerAdded
    }

    fun setPaymentModeIncome(mode: PaymentMode) {
        paymentMode = mode
    }
}