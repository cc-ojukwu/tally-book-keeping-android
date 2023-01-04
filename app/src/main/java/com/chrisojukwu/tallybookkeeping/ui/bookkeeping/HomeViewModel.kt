package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.*
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetExpenseListUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetIncomeListUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetLocalExpenseListUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetLocalIncomeListUseCase
import com.chrisojukwu.tallybookkeeping.utils.formatNumber
import com.chrisojukwu.tallybookkeeping.utils.getRandomRecordId
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.time.LocalDate
import com.chrisojukwu.tallybookkeeping.utils.Result
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.zip
import java.time.LocalTime
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getIncomeListUseCase: GetIncomeListUseCase,
    private val getExpenseListUseCase: GetExpenseListUseCase,
    private val getLocalIncomeListUseCase: GetLocalIncomeListUseCase,
    private val getLocalExpenseListUseCase: GetLocalExpenseListUseCase
) : ViewModel() {

    private val _allTimeIncome = MutableLiveData(BigDecimal.ZERO)
    val allTimeIncome: LiveData<String> = Transformations.switchMap(_allTimeIncome) { value -> formatNumber(value) }

    private val _allTimeExpense = MutableLiveData(BigDecimal.ZERO)
    val allTimeExpense: LiveData<String> = Transformations.switchMap(_allTimeExpense) { value -> formatNumber(value) }

    private val _allTimeBalance = MediatorLiveData<BigDecimal>()
    val allTimeBalance: LiveData<String> = Transformations.switchMap(_allTimeBalance) { value -> formatNumber(value) }


    private val _incomeToday = MutableLiveData(BigDecimal.ZERO)
    val incomeToday: LiveData<String> = Transformations.switchMap(_incomeToday) { value -> formatNumber(value) }

    private val _expenseToday = MutableLiveData(BigDecimal.ZERO)
    val expenseToday: LiveData<String> = Transformations.switchMap(_expenseToday) { value -> formatNumber(value) }

    private val _balanceToday = MediatorLiveData<BigDecimal>()
    val balanceToday: LiveData<String> = Transformations.switchMap(_balanceToday) { value -> formatNumber(value) }

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _displayList = MutableLiveData<MutableList<RecordHolder>>(mutableListOf())
    val displayList: LiveData<MutableList<RecordHolder>> = _displayList

    private val _transactionsList = MutableLiveData<List<RecordHolder>>()
    val transactionList: LiveData<List<RecordHolder>> = _transactionsList

    init {
        getTransactionsList()
    }

    private fun getTransactionsList() {
        _isLoading.value = true
        getIncomeListUseCase()
            .zip(getExpenseListUseCase()) { incomeResult, expenseResult ->
                if (incomeResult is Result.Success && expenseResult is Result.Success) {
                    _transactionsList.value = incomeResult.data + expenseResult.data
                    sortList()
                } else {
                    getLocalData()
                }
            }.launchIn(viewModelScope)
    }

    private fun getLocalData() {
        getLocalIncomeListUseCase().zip(getLocalExpenseListUseCase()) { incomeList, expenseList ->
            _transactionsList.value = incomeList + expenseList
            sortList()
        }.launchIn(viewModelScope)
    }


    private fun sortList() {
        val newList = mutableListOf<RecordHolder>()
        _transactionsList.value?.forEach { record ->
            when (record) {
                is RecordHolder.Income -> {
                    record.paymentList.forEach {
                        newList.add(
                            RecordHolder.Income(
                                record.recordId,
                                it.paymentDate,
                                BigDecimal.ZERO,
                                it.paymentAmount,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO,
                                record.description,
                                mutableListOf(),
                                record.customer,
                                mutableListOf()
                            )
                        )
                    }
                }
                is RecordHolder.Expense -> {
                    record.paymentList.forEach {
                        newList.add(
                            RecordHolder.Expense(
                                record.recordId,
                                it.paymentDate,
                                BigDecimal.ZERO,
                                it.paymentAmount,
                                BigDecimal.ZERO,
                                record.description,
                                record.category,
                                mutableListOf(),
                                record.supplier,
                                mutableListOf()
                            )
                        )
                    }
                }
                is RecordHolder.Header -> {}
            }
        }

        val sortedList = newList.sortedByDescending { it.date }
        val transactionsByDate = sortedList.groupBy { it.date.toLocalDate() }
        _displayList.value = processList(transactionsByDate)
        _allTimeBalance.value = _allTimeIncome.value?.minus(_allTimeExpense.value!!)
        _balanceToday.value = _incomeToday.value?.minus(_expenseToday.value!!)
        _isLoading.value = false

    }

    private fun processList(recordsByDate: Map<LocalDate, List<RecordHolder>>): MutableList<RecordHolder> {
        val finalList = mutableListOf<RecordHolder>()

        recordsByDate.keys.forEach {
            var incomeSum = BigDecimal.ZERO
            var expenseSum = BigDecimal.ZERO
            val records = recordsByDate[it]
            val incomeExpenseList = mutableListOf<RecordHolder>()

            if (records != null) {
                for (record in records) {
                    when (record) {
                        is RecordHolder.Income -> {
                            incomeSum += record.amountReceived
                            _allTimeIncome.value = _allTimeIncome.value?.plus(record.amountReceived)
                            incomeExpenseList.add(record)
                            if (it == LocalDate.now()) {
                                _incomeToday.value = _incomeToday.value?.plus(record.amountReceived)
                            }
                        }
                        is RecordHolder.Expense -> {
                            expenseSum += record.amountPaid
                            _allTimeExpense.value = _allTimeExpense.value?.plus(record.amountPaid)
                            incomeExpenseList.add(record)
                            if (it == LocalDate.now()) {
                                _expenseToday.value = _expenseToday.value?.plus(record.amountPaid)
                            }
                        }
                        else -> {}
                    }
                }
                finalList.add(
                    RecordHolder.Header(
                        getRandomRecordId(),
                        OffsetDateTime.of(it, LocalTime.now(), OffsetDateTime.now().offset),
                        incomeSum,
                        expenseSum,
                        incomeSum - expenseSum
                    )
                )
                finalList.addAll(incomeExpenseList)
            }
        }
        return finalList
    }

    fun getRecordUsingId(recordId: String): RecordHolder? {
        return _transactionsList.value?.find { it.recordId == recordId }
    }


}
