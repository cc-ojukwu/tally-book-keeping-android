package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedTransactionsViewModel @Inject constructor(
    val repository: RecordsRepository
) : ViewModel() {

    private val _transactionsList = MutableLiveData<MutableList<RecordHolder>>()
    val transactionsList: LiveData<MutableList<RecordHolder>> = _transactionsList

    fun setTransactionList(list: MutableList<RecordHolder>) {
        _transactionsList.value = list
    }



}


/*

private val testList = mutableListOf(
        RecordHolder.Income(
            recordId = "134227",
            date = LocalDateTime.of(2022, 3, 10, 13, 44),
            totalAmount = 4500.00.toBigDecimal(),
            amountReceived = 3600.00.toBigDecimal(),
            discount = 900.00.toBigDecimal(),
            subTotal = 3600.00.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "5 meat, 1 fish",
            productList = mutableListOf(
                Product("564", "meat", 500.00.toBigDecimal(), 5, 2500.00.toBigDecimal()),
                Product("780", "fish", 1100.00.toBigDecimal(), 1, 1100.00.toBigDecimal())
            ),
            customer = Customer("Emeka", "07438938753"),
            paymentList = mutableListOf(
                Payment(1700.00.toBigDecimal(), LocalDateTime.of(2022, 9, 15, 13, 44), PaymentMode.POS),
                Payment(1900.00.toBigDecimal(), LocalDateTime.of(2022, 10, 22, 20, 15), PaymentMode.CASH)
            )
        ),
        RecordHolder.Expense(
            recordId = "3745241",
            date = LocalDateTime.of(2022, 1, 14, 17, 24),
            totalAmount = 1200.00.toBigDecimal(),
            amountPaid = 1200.00.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "2 honey",
            category = null,
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
            balanceDue = BigDecimal.ZERO,
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
            category = null,
            supplier = Supplier("Nonso", "08069967467"),
            paymentList = mutableListOf(
                Payment(600.00.toBigDecimal(), LocalDateTime.of(2022, 7, 11, 7, 22), PaymentMode.BANK_TRANSFER),
            )
        ),
        RecordHolder.Expense(
            recordId = "1728365",
            date = LocalDateTime.of(2022, 7, 11, 15, 12),
            totalAmount = 400.55.toBigDecimal(),
            amountPaid = 400.55.toBigDecimal(),
            balanceDue = BigDecimal.ZERO,
            description = "1 bicycle",
            category = null,
            supplier = Supplier("Ugoo", "0806321367"),
            paymentList = mutableListOf(
                Payment(400.55.toBigDecimal(), LocalDateTime.of(2022, 8, 5, 17, 44), PaymentMode.CASH)
            )
        ),
        RecordHolder.Expense(
            recordId = "9102144",
            date = LocalDateTime.of(2022, 12, 22, 12, 12),
            totalAmount = 2500.00.toBigDecimal(),
            amountPaid = 2000.00.toBigDecimal(),
            balanceDue = 500.00.toBigDecimal(),
            description = "5 bottles coke",
            category = null,
            supplier = Supplier("Nkoli", "0806323457"),
            paymentList = mutableListOf(
                Payment(2000.00.toBigDecimal(), LocalDateTime.of(2022, 12, 9, 15, 12), PaymentMode.POS)
            )
        ),
        RecordHolder.Income(
            recordId = "1237254",
            date = LocalDateTime.of(2022, 12, 22, 10, 30),
            totalAmount = 1500.00.toBigDecimal(),
            amountReceived = 1500.00.toBigDecimal(),
            discount = BigDecimal.ZERO,
            subTotal = 1500.00.toBigDecimal(),
            balanceDue = 900.00.toBigDecimal(),
            description = "3 books",
            customer = Customer("Oge", "0743669653"),
            paymentList = mutableListOf(
                Payment(600.00.toBigDecimal(), LocalDateTime.of(2022, 12, 9, 20, 30), PaymentMode.CASH),
            )
        )
    )


 */