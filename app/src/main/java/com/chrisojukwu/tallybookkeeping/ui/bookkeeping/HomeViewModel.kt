package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.*
import com.chrisojukwu.tallybookkeeping.data.models.Customer
import com.chrisojukwu.tallybookkeeping.data.models.PaymentMode
import com.chrisojukwu.tallybookkeeping.data.models.RecordHolder
import com.chrisojukwu.tallybookkeeping.data.models.Supplier
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _allTimeIncome = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val allTimeIncome: LiveData<BigDecimal> = _allTimeIncome

    private val _allTimeExpense = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val allTimeExpense: LiveData<BigDecimal> = _allTimeExpense

    private val _allTimeBalance = MediatorLiveData<BigDecimal>()

    val allTimeBalance: LiveData<BigDecimal> = _allTimeBalance


    private val _incomeToday = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val incomeToday: LiveData<BigDecimal> = _incomeToday

    private val _expenseToday = MutableLiveData<BigDecimal>(BigDecimal.ZERO)
    val expenseToday: LiveData<BigDecimal> = _expenseToday

    private val _balanceToday = MediatorLiveData<BigDecimal>()
    val balanceToday: LiveData<BigDecimal> = _balanceToday


    private val _displayList = MutableLiveData<MutableList<RecordHolder>>(mutableListOf())
    val displayList: LiveData<MutableList<RecordHolder>> = _displayList


    private val list = mutableListOf(
        RecordHolder.Income(
            date = LocalDateTime.of(2022, 3, 10, 13, 44),
            totalAmount = 4500.00.toBigDecimal(),
            amountReceived = 3600.00.toBigDecimal(),
            discount = 900.00.toBigDecimal(),
            subTotal = 3600.00.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "1 cake",
            customer = Customer("Emeka", "07438938753"),
            paymentMode = PaymentMode.CASH
        ),
        RecordHolder.Expense(
            date = LocalDateTime.of(2022, 1, 14, 17, 24),
            totalAmount = 1200.00.toBigDecimal(),
            amountPaid = 1200.00.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "2 honey",
            supplier = Supplier("Obinna", "08067387647"),
            paymentMode = PaymentMode.POS
        ),
        RecordHolder.Income(
            date = LocalDateTime.of(2021, 5, 5, 22, 30),
            totalAmount = 1000.00.toBigDecimal(),
            amountReceived = 600.00.toBigDecimal(),
            discount = 100.00.toBigDecimal(),
            subTotal = 900.00.toBigDecimal(),
            balanceDue = 300.00.toBigDecimal(),
            description = "5 drinks",
            customer = Customer("Chioma", "0743883653"),
            paymentMode = PaymentMode.BANK_TRANSFER
        ),
        RecordHolder.Expense(
            date = LocalDateTime.of(2022, 7, 11, 7, 22),
            totalAmount = 600.00.toBigDecimal(),
            amountPaid = 600.00.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "2 honey",
            supplier = Supplier("Nonso", "08069967467"),
            paymentMode = PaymentMode.POS
        ),
        RecordHolder.Expense(
            date = LocalDateTime.of(2022, 7, 11, 15, 12),
            totalAmount = 400.55.toBigDecimal(),
            amountPaid = 400.55.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "1 bicycle",
            supplier = Supplier("Ugoo", "0806321367"),
            paymentMode = PaymentMode.CASH
        ),
        RecordHolder.Expense(
            date = LocalDateTime.of(2022, 12, 1, 15, 12),
            totalAmount = 2500.00.toBigDecimal(),
            amountPaid = 2000.00.toBigDecimal(),
            balanceDue = 500.00.toBigDecimal(),
            description = "5 bottles coke",
            supplier = Supplier("Nkoli", "0806323457"),
            paymentMode = PaymentMode.POS
        ),
        RecordHolder.Income(
            date = LocalDateTime.of(2022, 12, 1, 20, 30),
            totalAmount = 1500.00.toBigDecimal(),
            amountReceived = 1500.00.toBigDecimal(),
            discount = BigDecimal.ZERO,
            subTotal = 1500.00.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "3 books",
            customer = Customer("Oge", "0743669653"),
            paymentMode = PaymentMode.CASH
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
        val sortedList = list.sortedByDescending { it.date }
        val recordsByDate = sortedList.groupBy { it.date.toLocalDate() }
        _displayList.value = processList(recordsByDate)
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

    private fun formatNumber(number: BigDecimal): String {
        //val locale = Locale("en", "UK")
        val decFormat = DecimalFormat("###,###.##")
        decFormat.isGroupingUsed = true
        decFormat.groupingSize = 3

        return decFormat.format(number)
    }

}