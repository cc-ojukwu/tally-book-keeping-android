package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisojukwu.tallybookkeeping.domain.model.*
import com.chrisojukwu.tallybookkeeping.domain.usecase.DeleteExpenseUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.DeleteIncomeUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.UpdateExpenseUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.UpdateIncomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.chrisojukwu.tallybookkeeping.utils.getRandomPaymentId
import kotlinx.coroutines.flow.*
import java.time.OffsetDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class IncomeExpenseDetailsViewModel @Inject constructor(
    private val deleteIncomeUseCase: DeleteIncomeUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val updateIncomeUseCase: UpdateIncomeUseCase,
    private val updateExpenseUseCase: UpdateExpenseUseCase
) : ViewModel() {

    private val _recordToDisplay = MutableLiveData<RecordHolder>()
    val record: LiveData<RecordHolder> = _recordToDisplay

    private val _totalAmount = MutableLiveData<BigDecimal>()
    val totalAmount: LiveData<BigDecimal> = _totalAmount

    private val _discountAmount = MutableLiveData(BigDecimal.ZERO)
    val discountAmount: LiveData<BigDecimal> = _discountAmount

    private val _recordTitle = MutableLiveData<String>()
    val recordTitle: LiveData<String> = _recordTitle

    private val _recordDate = MutableLiveData<OffsetDateTime>()
    val recordDate: LiveData<OffsetDateTime> = _recordDate

    private val _customerName = MutableLiveData<String>()
    val customerName: LiveData<String> = _customerName

    private val _customerPhone = MutableLiveData<String>()
    val customerPhone: LiveData<String> = _customerPhone

    private val _itemList = MutableLiveData<List<Product>>()
    val itemList: LiveData<List<Product>?> = _itemList

    private val _paymentList = MutableLiveData<List<Payment>>()
    val paymentList: LiveData<List<Payment>> = _paymentList

    private val _isFullyPaid = MutableLiveData(true)
    val isFullyPaid: LiveData<Boolean> = _isFullyPaid

    private val _updateRecordResult = MutableStateFlow<Result<StringResponse>>(Result.Loading)
    val updateRecordResult: StateFlow<Result<StringResponse>> = _updateRecordResult


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
                    _itemList.value = record.productList!!
                } else {
                    _itemList.value =
                        mutableListOf(Product("8485", record.description, record.subTotal, 1, record.subTotal))
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
                    _itemList.value = record.productList!!
                } else {
                    _itemList.value =
                        mutableListOf(Product("4656", record.description, record.totalAmount, 1, record.totalAmount))
                }
                _paymentList.value = record.paymentList
                _isFullyPaid.value = record.balanceDue == BigDecimal.ZERO
            }
            is RecordHolder.Header -> {}
        }
    }

    fun deleteIncome(): Flow<Result<StringResponse>> =
        deleteIncomeUseCase(_recordToDisplay.value as RecordHolder.Income)

    fun deleteExpense(): Flow<Result<StringResponse>> =
        deleteExpenseUseCase(_recordToDisplay.value as RecordHolder.Expense)

    fun addNewPayment(paymentAmount: BigDecimal) {
        when (_recordToDisplay.value) {
            is RecordHolder.Income -> {
                (_recordToDisplay.value as RecordHolder.Income)
                    .paymentList.add(
                        Payment(
                            getRandomPaymentId(),
                            paymentAmount,
                            OffsetDateTime.now(ZoneId.systemDefault()),
                            PaymentMode.CASH
                        )
                    )
                updateIncomeUseCase(_recordToDisplay.value as RecordHolder.Income)
                    .onEach { result ->
                        _updateRecordResult.value = result
                    }.launchIn(viewModelScope)
            }
            is RecordHolder.Expense -> {
                (_recordToDisplay.value as RecordHolder.Expense)
                    .paymentList.add(
                        Payment(
                            getRandomPaymentId(),
                            paymentAmount,
                            OffsetDateTime.now(ZoneId.systemDefault()),
                            PaymentMode.CASH
                        )
                    )
                updateExpenseUseCase(_recordToDisplay.value as RecordHolder.Expense)
                    .onEach { result ->
                        _updateRecordResult.value = result
                    }.launchIn(viewModelScope)
            }
            else -> {}
        }
    }


}