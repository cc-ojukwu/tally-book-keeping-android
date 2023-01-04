package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.*
import com.chrisojukwu.tallybookkeeping.domain.model.Payment
import com.chrisojukwu.tallybookkeeping.domain.model.Product
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import com.chrisojukwu.tallybookkeeping.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val preferencesStorage: PreferenceStorage,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _businessName = MutableLiveData<String>()
    val businessName: LiveData<String> = _businessName

    private val _businessAddress = MutableLiveData<String>()
    val businessAddress: LiveData<String> = _businessAddress

    private val _customerName = MutableLiveData<String>("")
    val customerName: LiveData<String> = _customerName

    private val _customerPhone = MutableLiveData<String>("")
    val customerPhone: LiveData<String> = _customerPhone

    private val _paymentDate = MutableLiveData<OffsetDateTime>()
    val paymentDate: LiveData<OffsetDateTime> = _paymentDate

    private val _recordId = MutableLiveData<String>()
    val recordId: LiveData<String> = _recordId

    private val _receiptNumber = MutableLiveData<String>()
    val receiptNumber: LiveData<String> = _receiptNumber

    private val _receiptAmount = MutableLiveData<BigDecimal>()
    val receiptAmount: LiveData<BigDecimal> = _receiptAmount

    private val _balanceDue = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val balanceDue: LiveData<BigDecimal> = _balanceDue

    private val _paymentMethod = MutableLiveData<String>()
    val paymentMethod: LiveData<String> = _paymentMethod

    private val _itemList = MutableLiveData<List<Product>>(mutableListOf())
    val itemList: LiveData<List<Product>> = _itemList

    fun setReceiptDetails(payment: Payment, record: RecordHolder?) {
        viewModelScope.launch {
            preferencesStorage.getBusinessName.flowOn(ioDispatcher).collect { value ->
                value.let { _businessName.value = it }
            }
            preferencesStorage.getBusinessAddress.flowOn(ioDispatcher).collect { value ->
                value.let { _businessAddress.value = it }
            }
            when (record) {
                is RecordHolder.Income -> {
                    _customerName.value = record.customer?.customerName
                    _customerPhone.value = record.customer?.customerPhone
                    _paymentDate.value = payment.paymentDate
                    _recordId.value = record.recordId
                    _receiptNumber.value = payment.id
                    _receiptAmount.value = payment.paymentAmount
                    _balanceDue.value = record.balanceDue
                    _paymentMethod.value = payment.paymentMode.toString()
                    if (record.productList?.size!! > 0) {
                        _itemList.value = record.productList!!
                    } else {
                        _itemList.value =
                            mutableListOf(Product("8485", record.description, record.subTotal, 1, record.subTotal))
                    }
                }
                is RecordHolder.Expense -> {
                    _customerName.value = record.supplier?.supplierName
                    _customerPhone.value = record.supplier?.supplierPhone
                    _paymentDate.value = payment.paymentDate
                    _recordId.value = record.recordId
                    _receiptNumber.value = payment.id
                    _receiptAmount.value = payment.paymentAmount
                    _balanceDue.value = record.balanceDue
                    _paymentMethod.value = payment.paymentMode.toString()
                    if (record.productList?.size!! > 0) {
                        _itemList.value = record.productList!!
                    } else {
                        _itemList.value =
                            mutableListOf(
                                Product(
                                    "8485",
                                    record.description,
                                    record.totalAmount,
                                    1,
                                    record.totalAmount
                                )
                            )
                    }
                }
                else -> {}
            }
        }

    }

}