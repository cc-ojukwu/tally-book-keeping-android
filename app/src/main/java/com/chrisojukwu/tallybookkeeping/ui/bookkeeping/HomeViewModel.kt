package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.*
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import com.chrisojukwu.tallybookkeeping.di.IoDispatcher
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.domain.usecase.*
import com.chrisojukwu.tallybookkeeping.utils.getRandomRecordId
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.time.LocalDate
import com.chrisojukwu.tallybookkeeping.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalTime
import java.time.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getIncomeListUseCase: GetIncomeListUseCase,
    private val getExpenseListUseCase: GetExpenseListUseCase,
    private val getLocalIncomeListUseCase: GetLocalIncomeListUseCase,
    private val getLocalExpenseListUseCase: GetLocalExpenseListUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _allTimeIncome = MutableLiveData(BigDecimal.ZERO)
    val allTimeIncome: LiveData<BigDecimal> = _allTimeIncome

    private val _allTimeExpense = MutableLiveData(BigDecimal.ZERO)
    val allTimeExpense: LiveData<BigDecimal> = _allTimeExpense

    private val _allTimeBalance = MutableLiveData(BigDecimal.ZERO)
    val allTimeBalance: LiveData<BigDecimal> = _allTimeBalance

    private val _incomeToday = MutableLiveData(BigDecimal.ZERO)
    val incomeToday: LiveData<BigDecimal> = _incomeToday

    private val _expenseToday = MutableLiveData(BigDecimal.ZERO)
    val expenseToday: LiveData<BigDecimal> = _expenseToday

    private val _balanceToday = MutableLiveData<BigDecimal>()
    val balanceToday: LiveData<BigDecimal> = _balanceToday

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _responseReceived = MutableLiveData(false)
    val responseReceived: LiveData<Boolean> = _responseReceived

    private val _displayList = MutableLiveData<MutableList<RecordHolder>>(mutableListOf())
    val displayList: LiveData<MutableList<RecordHolder>> = _displayList

    private val _transactionsList = MutableLiveData<List<RecordHolder>>()
    val transactionList: LiveData<List<RecordHolder>> = _transactionsList

    private val _businessName = MutableLiveData<String>()
    val businessName: LiveData<String> = _businessName

    init {
        getUserInfo()
        fetchTransactionsList()
    }

    fun refreshBusinessName() {
        viewModelScope.launch {
            preferenceStorage.getBusinessName()
                .collect { result ->
                    _businessName.postValue(result)
                }
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch (ioDispatcher) {
            getUserInfoUseCase()
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            _businessName.postValue(result.data.businessName)
                            preferenceStorage.saveUserEmail(result.data.email)
                            preferenceStorage.saveUserFirstName(result.data.firstName)
                            preferenceStorage.saveUserLastName(result.data.lastName)
                            preferenceStorage.saveBusinessName(result.data.businessName)
                            preferenceStorage.saveBusinessAddress(result.data.businessAddress)
                            preferenceStorage.saveBusinessPhone(result.data.businessPhone)
                        }
                        else -> {}
                    }
                }
        }
    }

    private fun fetchTransactionsList() {
        _isLoading.value = true
        getIncomeListUseCase()
            .combine(getExpenseListUseCase()) { incomeResult, expenseResult ->
                if (incomeResult is Result.Success && expenseResult is Result.Success) {
                    _transactionsList.value = incomeResult.data + expenseResult.data
                    sortList()
                } else {
                    getLocalData()
                }
            }.launchIn(viewModelScope)
    }

    private fun getLocalData() {
        getLocalIncomeListUseCase().combine(getLocalExpenseListUseCase()) { incomeList, expenseList ->
            _transactionsList.value = incomeList + expenseList
            sortList()
        }.launchIn(viewModelScope)
    }


    //sort the records by payment so that each new record in the final list
    // represents a separate payment
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
        _displayList.value = addHeaders(transactionsByDate)
        _allTimeBalance.value = _allTimeIncome.value?.minus(_allTimeExpense.value!!)
        _balanceToday.value = _incomeToday.value?.minus(_expenseToday.value!!)
        _responseReceived.value = true
        _isLoading.value = false
    }

    private fun addHeaders(recordsByDate: Map<LocalDate, List<RecordHolder>>): MutableList<RecordHolder> {
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
