package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.*
import com.chrisojukwu.tallybookkeeping.domain.model.*
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.chrisojukwu.tallybookkeeping.domain.usecase.UpdateIncomeUseCase
import com.chrisojukwu.tallybookkeeping.utils.formatDateToString
import com.chrisojukwu.tallybookkeeping.utils.getRandomPaymentId
import com.chrisojukwu.tallybookkeeping.utils.notifyObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class EditIncomeViewModel @Inject constructor(
    private val updateIncomeUseCase: UpdateIncomeUseCase
) : ViewModel() {

    private val _recordToEdit = MutableLiveData<RecordHolder.Income?>()
    val recordToEdit: LiveData<RecordHolder.Income?> = _recordToEdit

    private var _transactionDate = MutableLiveData(OffsetDateTime.now(ZoneId.systemDefault()))
    val transactionDate: LiveData<String> =
        Transformations.switchMap(_transactionDate) { date -> formatDateToString(date) }

    private val _discountIsPercent = MutableLiveData(true)

    private val _showPercent = MutableLiveData(true)
    val showPercent: LiveData<Boolean> = _showPercent

    private val _discountAmount = MutableLiveData(BigDecimal.ZERO)
    val discountAmount: LiveData<BigDecimal> = _discountAmount

    private val _discountPercentage = MutableLiveData(0.0)
    val discountPercentage: LiveData<Double> = _discountPercentage

    private val _totalAmount = MutableLiveData(BigDecimal.ZERO)
    val totalAmount: LiveData<BigDecimal> = _totalAmount

    private val _subTotalAmount =
        MutableLiveData(_totalAmount.value!!.minus(_discountAmount.value!!))
    val subTotalAmount: LiveData<BigDecimal> = _subTotalAmount

    private val _amountReceived = MutableLiveData(BigDecimal.ZERO)
    val amountReceived: LiveData<BigDecimal> = _amountReceived

    private val _balanceDue = MutableLiveData(BigDecimal.ZERO)
    val balanceDue: LiveData<BigDecimal> = _balanceDue

    val isDiscountAdded = MutableLiveData(false)

    private val _productList = MutableLiveData<MutableList<Product>>(mutableListOf())
    val productList: LiveData<MutableList<Product>> = _productList

    private val _productCount = Transformations.map(_productList) { list -> list.size }
    val productCount = _productCount

    private val _itemsTotalCost = MutableLiveData(BigDecimal.ZERO)
    val itemsTotalCost: LiveData<BigDecimal> = _itemsTotalCost

    private val _isCustomerRequired = MutableLiveData(false)
    val isCustomerRequired: LiveData<Boolean> = _isCustomerRequired

    private val _isCustomerAdded = MutableLiveData(false)
    val isCustomerAdded: LiveData<Boolean> = _isCustomerAdded

    private val _customerInfo = MutableLiveData<Customer?>()
    val customerInfo: LiveData<Customer?> = _customerInfo

    private val _description = MutableLiveData("")

    private var _paymentMode: PaymentMode = PaymentMode.CASH

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    fun saveDate(date: OffsetDateTime) {
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

    fun addListToProductList(productList: MutableList<Product>) {
        _productList.value?.addAll(productList)
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

    private fun updateItemsTotalCost() {
        _itemsTotalCost.value = _productList.value?.sumOf { it.productTotalPrice }
    }

    fun updateCustomerInfo(customer: Customer) {
        _customerInfo.value = customer
    }

    private fun updateSubtotal() {
        _subTotalAmount.value = _totalAmount.value!! - _discountAmount.value!!
        updateBalanceDue()
    }

    fun updateBalanceDue() {
        _balanceDue.value = _subTotalAmount.value!! - _amountReceived.value!!
    }

    fun updateDescription(desc: String) {
        _description.value = desc
    }

    fun saveEditIncomeDetails(): StateFlow<Result<StringResponse>> =
        updateIncomeUseCase(
            RecordHolder.Income(
                recordId = _recordToEdit.value!!.recordId,
                date = _transactionDate.value!!,
                totalAmount = _totalAmount.value!!,
                amountReceived = _amountReceived.value!!,
                discount = _discountAmount.value!!,
                subTotal = _subTotalAmount.value!!,
                balanceDue = _balanceDue.value!!,
                description = _description.value!!,
                productList = _productList.value,
                customer = customerInfo.value,
                paymentList = mutableListOf(
                    Payment(getRandomPaymentId(), _amountReceived.value!!, _transactionDate.value!!, _paymentMode)
                )
            )
        ).stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Result.Loading)


    fun setCustomerRequirement(isCustomerRequired: Boolean) {
        _isCustomerRequired.value = isCustomerRequired
    }

    fun customerAdded(customerAdded: Boolean) {
        _isCustomerAdded.value = customerAdded
    }

    fun setPaymentModeIncome(mode: PaymentMode) {
        _paymentMode = mode
    }

    fun setRecordToEdit(record: RecordHolder.Income) {
        _recordToEdit.value = record
    }

    fun resetDataFields() {
        _productList.value = mutableListOf()
        isDiscountAdded.value = false
        _recordToEdit.value = null
        _discountIsPercent.value = true
        _transactionDate.value = OffsetDateTime.now(ZoneId.systemDefault())
        _showPercent.value = true
        _discountAmount.value = BigDecimal.ZERO
        _discountPercentage.value = 0.0
        isDiscountAdded.value = false
        _description.value = ""
        _customerInfo.value = null
        _isCustomerAdded.value = false
        _isCustomerRequired.value = false
        _itemsTotalCost.value = BigDecimal.ZERO
    }
}