package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chrisojukwu.tallybookkeeping.data.models.Payment
import com.chrisojukwu.tallybookkeeping.data.models.Product
import com.chrisojukwu.tallybookkeeping.data.models.RecordHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class IncomeExpenseDetailsViewModel @Inject constructor() : ViewModel() {

    private val _recordToDisplay = MutableLiveData<RecordHolder>()

    private val _totalAmount = MutableLiveData<BigDecimal>()
    val totalAmount: LiveData<BigDecimal> = _totalAmount

    private val _discountAmount = MutableLiveData(BigDecimal.ZERO)
    val discountAmount: LiveData<BigDecimal> = _discountAmount

    private val _recordTitle = MutableLiveData<String>()
    val recordTitle: LiveData<String> = _recordTitle

    private val _recordDate = MutableLiveData<LocalDateTime>()
    val recordDate: LiveData<LocalDateTime> = _recordDate

    private val _customerName = MutableLiveData<String>()
    val customerName: LiveData<String> = _customerName

    private val _customerPhone = MutableLiveData<String>()
    val customerPhone: LiveData<String> = _customerPhone

    private val _itemList = MutableLiveData<List<Product>?>()
    val itemList: LiveData<List<Product>?> = _itemList

    private val _paymentList = MutableLiveData<List<Payment>>()
    val paymentList: LiveData<List<Payment>> = _paymentList

    private val _isFullyPaid = MutableLiveData(true)
    val isFullyPaid: LiveData<Boolean> = _isFullyPaid


    fun setRecord(record: RecordHolder) {
        _recordToDisplay.value = record

        when (record) {
            is RecordHolder.Income -> {
                _totalAmount.value = record.subTotal
                _discountAmount.value = record.discount
                _recordTitle.value = "Income Transaction"
                _recordDate.value = record.date
                _customerName.value = record.customer?.customerName
                _customerPhone.value = record.customer?.customerPhone
                if (record.productList?.size!! > 0) {
                    _itemList.value = record.productList
                } else {
                    _itemList.value =
                        mutableListOf(Product("542", record.description, record.subTotal, 1, record.subTotal))
                }
                _paymentList.value = record.paymentList
                _isFullyPaid.value = record.balanceDue == BigDecimal.ZERO
            }
            is RecordHolder.Expense -> {
                _totalAmount.value = record.totalAmount
                _recordTitle.value = "Expense Transaction"
                _recordDate.value = record.date
                _customerName.value = record.supplier?.supplierName
                _customerPhone.value = record.supplier?.supplierPhone
                if (record.productList?.size!! > 0) {
                    _itemList.value = record.productList
                } else {
                    _itemList.value =
                        mutableListOf(Product("456", record.description, record.totalAmount, 1, record.totalAmount))
                }
                _paymentList.value = record.paymentList
                _isFullyPaid.value = record.balanceDue == BigDecimal.ZERO
            }
            is RecordHolder.Header -> {}
        }
    }

}