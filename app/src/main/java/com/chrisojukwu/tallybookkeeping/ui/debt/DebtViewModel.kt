package com.chrisojukwu.tallybookkeeping.ui.debt

import androidx.lifecycle.*
import com.chrisojukwu.tallybookkeeping.domain.model.Payable
import com.chrisojukwu.tallybookkeeping.domain.model.Receivable
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetLocalExpenseListUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetLocalIncomeListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class DebtViewModel @Inject constructor(
    private val getLocalIncomeListUseCase: GetLocalIncomeListUseCase,
    private val getLocalExpenseListUseCase: GetLocalExpenseListUseCase
) : ViewModel() {

    private var _transactionList = listOf<RecordHolder>()

    private val _receivableList = MutableLiveData<List<Receivable>>()
    val receivableList: LiveData<List<Receivable>> = _receivableList

    private val _payableList = MutableLiveData<MutableList<Payable>>()
    val payableList: LiveData<MutableList<Payable>> = _payableList

    private val _receivableSum = MutableLiveData(BigDecimal.ZERO)
    val receivableSum: LiveData<BigDecimal> = _receivableSum

    private val _payableSum = MutableLiveData(BigDecimal.ZERO)
    val payableSum: LiveData<BigDecimal> = _payableSum

    private val _responseReceived = MutableLiveData(false)
    val responseReceived: LiveData<Boolean> = _responseReceived

    init {
        getIncomeExpenseList()
    }

    private fun getIncomeExpenseList() {
        getLocalIncomeListUseCase()
            .combine (getLocalExpenseListUseCase()) { incomeList, expenseList ->
                _transactionList = incomeList + expenseList
                getDebtList()
            }.launchIn(viewModelScope)
    }

    private fun getDebtList() {
        viewModelScope.launch {
            val receivablesList = mutableListOf<Receivable>()
            val payablesList = mutableListOf<Payable>()
            _transactionList.forEach { record ->
                when (record) {
                    is RecordHolder.Income -> {
                        if (record.balanceDue.compareTo(BigDecimal.ZERO) != 0) {
                            val receivable = Receivable(record.recordId, record.balanceDue, record.customer!!, record.date)
                            receivablesList.add(receivable)
                        }
                    }
                    is RecordHolder.Expense -> {
                        if (record.balanceDue.compareTo(BigDecimal.ZERO) != 0) {
                            val payable = Payable(record.recordId, record.balanceDue, record.supplier!!, record.date)
                            payablesList.add(payable)
                        }
                    }
                    else -> {}
                }
            }
            _receivableList.value = receivablesList
            _payableList.value = payablesList

            _receivableSum.value = receivablesList.sumOf { it.amount }
            _payableSum.value = payablesList.sumOf { it.amount }
            _responseReceived.value = true
        }
    }


}