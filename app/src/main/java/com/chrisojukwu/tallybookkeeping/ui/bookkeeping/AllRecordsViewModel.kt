package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.*
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetLocalExpenseListUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetLocalIncomeListUseCase
import com.chrisojukwu.tallybookkeeping.utils.getRandomRecordId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.zip
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class AllRecordsViewModel @Inject constructor(
    private val getLocalIncomeListUseCase: GetLocalIncomeListUseCase,
    private val getLocalExpenseListUseCase: GetLocalExpenseListUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _allRecordsList = MutableLiveData<List<RecordHolder>>()
    val allRecordsList: LiveData<List<RecordHolder>> = _allRecordsList

    private val _sortedRecordsList = MutableLiveData<List<RecordHolder>>()
    val sortedRecordsList: LiveData<List<RecordHolder>> = _sortedRecordsList

    private val _incomeSum = MutableLiveData(BigDecimal.ZERO)
    val incomeSum: LiveData<BigDecimal> = _incomeSum

    private val _expenseSum = MutableLiveData(BigDecimal.ZERO)
    val expenseSum: LiveData<BigDecimal> = _expenseSum


    init {
        getAllRecords()
    }

    private fun getAllRecords() {
        getLocalIncomeListUseCase().zip(getLocalExpenseListUseCase()) { incomeList, expenseList ->
            _allRecordsList.value = incomeList + expenseList
            sortList()
        }.launchIn(viewModelScope)
    }

    private fun sortList() {
        val sortedList = _allRecordsList.value?.sortedByDescending { it.date }
        val recordsByDate = sortedList?.groupBy { it.date.toLocalDate() }
        _sortedRecordsList.value = addHeaders(recordsByDate)
    }

    private fun addHeaders(recordsByDate: Map<LocalDate, List<RecordHolder>>?): List<RecordHolder> {
        val finalList = mutableListOf<RecordHolder>()
        recordsByDate?.keys?.forEach {
            var headerIncomeSum = BigDecimal.ZERO
            var headerExpenseSum = BigDecimal.ZERO
            val records = recordsByDate[it]
            val recordHolderList = mutableListOf<RecordHolder>()

            if (records != null) {
                for (record in records) {
                    when (record) {
                        is RecordHolder.Income -> {
                            headerIncomeSum += record.amountReceived
                            _incomeSum.value = _incomeSum.value?.plus(record.amountReceived)
                            recordHolderList.add(record)
                        }
                        is RecordHolder.Expense -> {
                            headerExpenseSum += record.amountPaid
                            _expenseSum.value = _expenseSum.value?.plus(record.amountPaid)
                            recordHolderList.add(record)
                        }
                        else -> {}
                    }
                }
                finalList.add(
                    RecordHolder.Header(
                        getRandomRecordId(),
                        OffsetDateTime.of(it, LocalTime.now(), OffsetDateTime.now().offset),
                        headerIncomeSum,
                        headerExpenseSum,
                        headerIncomeSum - headerExpenseSum
                    )
                )
                finalList.addAll(recordHolderList)
            }
        }
        return finalList
    }

}