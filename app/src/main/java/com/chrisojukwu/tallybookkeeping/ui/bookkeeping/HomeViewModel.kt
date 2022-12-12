package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.*
import com.chrisojukwu.tallybookkeeping.data.models.*
import com.chrisojukwu.tallybookkeeping.utils.formatNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

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

    private val _displayList = MutableLiveData<MutableList<RecordHolder>>(mutableListOf())
    val displayList: LiveData<MutableList<RecordHolder>> = _displayList


    private val testList = mutableListOf(
        RecordHolder.Income(
            recordId = "134227",
            date = LocalDateTime.of(2022, 3, 10, 13, 44),
            totalAmount = 4500.00.toBigDecimal(),
            amountReceived = 3600.00.toBigDecimal(),
            discount = 900.00.toBigDecimal(),
            subTotal = 3600.00.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "1 cake",
            productList = mutableListOf(
                Product("564", "meat", 500.00.toBigDecimal(), 5, 2500.00.toBigDecimal()),
                Product("780", "fish", 1100.00.toBigDecimal(), 1, 1100.00.toBigDecimal())
            ),
            customer = Customer("Emeka", "07438938753"),
            paymentList = mutableListOf(
                Payment(1500.00.toBigDecimal(), LocalDateTime.of(2022, 9, 15, 13, 44), PaymentMode.POS),
                Payment(900.00.toBigDecimal(), LocalDateTime.of(2022, 10, 22, 20, 15), PaymentMode.CASH)
            )
        ),
        RecordHolder.Expense(
            recordId = "3745241",
            date = LocalDateTime.of(2022, 1, 14, 17, 24),
            totalAmount = 1200.00.toBigDecimal(),
            amountPaid = 1200.00.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "2 honey",
            supplier = Supplier("Obinna", "08067387647"),
            paymentList = mutableListOf(
                Payment(1200.00.toBigDecimal(), LocalDateTime.of(2022, 1, 15, 13, 44), PaymentMode.POS)
            )
        ),
        RecordHolder.Income(
            recordId = "1374533",
            date = LocalDateTime.of(2021, 5, 5, 22, 30),
            totalAmount = 1000.00.toBigDecimal(),
            amountReceived = 600.00.toBigDecimal(),
            discount = 100.00.toBigDecimal(),
            subTotal = 900.00.toBigDecimal(),
            balanceDue = 300.00.toBigDecimal(),
            description = "5 drinks",
            customer = Customer("Chioma", "0743883653"),
            paymentList = mutableListOf(
                Payment(600.00.toBigDecimal(), LocalDateTime.of(2022, 9, 15, 13, 44), PaymentMode.POS),
                Payment(300.00.toBigDecimal(), LocalDateTime.of(2022, 11, 1, 13, 44), PaymentMode.POS)
            )
        ),
        RecordHolder.Expense(
            recordId = "4322105",
            date = LocalDateTime.of(2022, 7, 11, 7, 22),
            totalAmount = 600.00.toBigDecimal(),
            amountPaid = 600.00.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "2 honey",
            supplier = Supplier("Nonso", "08069967467"),
            paymentList = mutableListOf(
                Payment(1500.00.toBigDecimal(), LocalDateTime.of(2022, 7, 11, 7, 22), PaymentMode.BANK_TRANSFER),
            )
        ),
        RecordHolder.Expense(
            recordId = "1728365",
            date = LocalDateTime.of(2022, 7, 11, 15, 12),
            totalAmount = 400.55.toBigDecimal(),
            amountPaid = 400.55.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "1 bicycle",
            supplier = Supplier("Ugoo", "0806321367"),
            paymentList = mutableListOf(
                Payment(400.00.toBigDecimal(), LocalDateTime.of(2022, 8, 5, 17, 44), PaymentMode.CASH)
            )
        ),
        RecordHolder.Expense(
            recordId = "9102144",
            date = LocalDateTime.of(2022, 12, 9, 15, 12),
            totalAmount = 2500.00.toBigDecimal(),
            amountPaid = 2000.00.toBigDecimal(),
            balanceDue = 500.00.toBigDecimal(),
            description = "5 bottles coke",
            supplier = Supplier("Nkoli", "0806323457"),
            paymentList = mutableListOf(
                Payment(2000.00.toBigDecimal(), LocalDateTime.of(2022, 12, 9, 15, 12), PaymentMode.POS)
            )
        ),
        RecordHolder.Income(
            recordId = "1237254",
            date = LocalDateTime.of(2022, 12, 9, 20, 30),
            totalAmount = 1500.00.toBigDecimal(),
            amountReceived = 1500.00.toBigDecimal(),
            discount = BigDecimal.ZERO,
            subTotal = 1500.00.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "3 books",
            customer = Customer("Oge", "0743669653"),
            paymentList = mutableListOf(
                Payment(1500.00.toBigDecimal(), LocalDateTime.of(2022, 12, 9, 20, 30), PaymentMode.CASH),
            )
        )
    )

    init {
        sortList()
        callListeners()
    }

    private fun callListeners() {
        _allTimeBalance.addSource(_allTimeIncome) { updateAllTimeBalance() }
        _allTimeBalance.addSource(_allTimeExpense) { updateAllTimeBalance() }

        _balanceToday.addSource(_incomeToday) { updateBalanceToday() }
        _balanceToday.addSource(_expenseToday) { updateBalanceToday() }

    }

    private fun updateAllTimeBalance() {
        _allTimeBalance.value = _allTimeIncome.value?.minus(_allTimeExpense.value!!)
    }

    private fun updateBalanceToday() {
        _balanceToday.value = _incomeToday.value?.minus(_expenseToday.value!!)
    }

    private fun sortList() {
        val newList = mutableListOf<RecordHolder>()
        testList.forEach { record ->
            when (record) {
                is RecordHolder.Income -> {
                    record.paymentList.forEach {
                        newList.add(
                            RecordHolder.Income(
                                record.recordId,
                                it.date,
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
                                it.date,
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
                        "${(0..20).random()}${(0..20).random()}${(0..20).random()}",
                        LocalDateTime.of(it, LocalTime.now()),
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
        return testList.find { it.recordId == recordId }
    }


}
