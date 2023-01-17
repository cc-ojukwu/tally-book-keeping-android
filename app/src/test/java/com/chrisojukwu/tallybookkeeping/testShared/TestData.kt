package com.chrisojukwu.tallybookkeeping.testShared

import com.chrisojukwu.tallybookkeeping.data.dto.NetworkExpense
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkIncome
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkPayment
import com.chrisojukwu.tallybookkeeping.domain.model.*
import java.math.BigDecimal
import java.time.OffsetDateTime

object TestData {

    val date1 = OffsetDateTime.parse("2022-10-30T06:00:00Z")
    val date2 = OffsetDateTime.parse("2021-11-30T06:00:00Z")
    val date3 = OffsetDateTime.parse("2022-05-30T06:00:00Z")

    var returnedUser: User? = null

    val email = "userEmail"
    val firstName = "userFirstName"
    val lastName = "userLastName"
    val businessName = "userBusinessName"
    val businessAddress = "userBusinessAddress"
    val businessPhone = "userBusinessPhone"

    var savedEmail = ""
    var savedFirstName = ""
    var savedLastName = ""
    var savedBusinessName = ""
    var savedBusinessAddress = ""
    var savedBusinessPhone = ""

    val user = User(
        email,"4r949nf","user-49u49",Provider.LOCAL, firstName, lastName,
        businessName, businessAddress, businessPhone, "ROLE_USER",true
    )

    val dummyNetworkIncome = NetworkIncome(
        "a2", date2, BigDecimal.ZERO, BigDecimal("40.00"), BigDecimal.ZERO,
        BigDecimal.ZERO, BigDecimal.ZERO, "", mutableListOf(), null,
        mutableListOf(NetworkPayment("", BigDecimal("40.00"), date2, PaymentMode.POS))
    )
    val dummyIncome = RecordHolder.Income(
        "a2", date2, BigDecimal.ZERO, BigDecimal("40.00"), BigDecimal.ZERO,
        BigDecimal.ZERO, BigDecimal.ZERO, "", mutableListOf(), null,
        mutableListOf(Payment("", BigDecimal("40.00"), date2, PaymentMode.POS))
    )

    val dummyNetworkExpense = NetworkExpense(
        "b1", date3, BigDecimal.ZERO, BigDecimal("55.50"),BigDecimal.ZERO, "",
        null, mutableListOf(), null,
        mutableListOf(NetworkPayment("nd8hd", BigDecimal("55.50"), date3, PaymentMode.POS))
    )

    val dummyExpense = RecordHolder.Expense(
        "b1", date3, BigDecimal.ZERO, BigDecimal("55.50"),BigDecimal.ZERO, "",
        null, mutableListOf(), null,
        mutableListOf(Payment("nd8hd", BigDecimal("55.50"), date3, PaymentMode.POS))
    )

    val networkIncomeList = listOf(
        dummyNetworkIncome
    )

    val networkExpenseList = listOf(
        dummyNetworkExpense
    )

    val stockList = mutableListOf(
        InventoryItem("stock1", "1234", BigDecimal("2.00"), BigDecimal("13.50"), 1, null),
        InventoryItem("stock2", "5678", BigDecimal("4.00"), BigDecimal("11.50"), 6, null)
    )

    val incomeList = listOf(
        RecordHolder.Income(
            "a1", date1, BigDecimal.ZERO, BigDecimal("23.00"), BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, "", mutableListOf(), null,
            mutableListOf(Payment("", BigDecimal("23.00"), date1, PaymentMode.POS))
        ),
        RecordHolder.Income(
            "a2", date2, BigDecimal.ZERO, BigDecimal("40.00"), BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, "", mutableListOf(), null,
            mutableListOf(Payment("", BigDecimal("40.00"), date2, PaymentMode.POS))
        )
    )

    val localIncomeList = listOf(
        RecordHolder.Income(
            "c1", date1, BigDecimal.ZERO, BigDecimal("16.00"), BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal("4.00"), "", mutableListOf(), null,
            mutableListOf(Payment("", BigDecimal.ZERO, date1, PaymentMode.POS))
        ),
        RecordHolder.Income(
            "c2", date2, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, "", mutableListOf(), null,
            mutableListOf(Payment("", BigDecimal.ZERO, date2, PaymentMode.POS))
        )
    )

    val expenseList = listOf(
        RecordHolder.Expense(
            "b1", date3, BigDecimal.ZERO, BigDecimal("55.50"),BigDecimal.ZERO, "",
            null, mutableListOf(), null,
            mutableListOf(Payment("nd8hd", BigDecimal("55.50"), date3, PaymentMode.POS))
        )
    )

    val localExpenseList = listOf(
        RecordHolder.Expense(
            "d1", date3, BigDecimal.ZERO, BigDecimal.ZERO,BigDecimal("5.00"), "",
            null, mutableListOf(), null,
            mutableListOf(Payment("nd8hd", BigDecimal.ZERO, date3, PaymentMode.POS))
        )
    )

    val combineList = listOf(
        RecordHolder.Income(
            "a1", date1, BigDecimal.ZERO, BigDecimal("23.00"), BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, "", mutableListOf(), null,
            mutableListOf(Payment("", BigDecimal.ZERO, date1, PaymentMode.POS))
        ),
        RecordHolder.Expense(
            "b1", date3, BigDecimal.ZERO, BigDecimal("55.50"),BigDecimal.ZERO, "",
            null, mutableListOf(), null,
            mutableListOf(Payment("nd8hd", BigDecimal.ZERO, date3, PaymentMode.POS))
        ),
        RecordHolder.Income(
            "a2", date2, BigDecimal.ZERO, BigDecimal("40.00"), BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, "", mutableListOf(), null,
            mutableListOf(Payment("", BigDecimal.ZERO, date2, PaymentMode.POS))
        )
    )

    val combineLocalList = listOf(
        RecordHolder.Income(
            "c1", date1, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, "", mutableListOf(), null,
            mutableListOf(Payment("", BigDecimal.ZERO, date1, PaymentMode.POS))
        ),
        RecordHolder.Income(
            "c2", date2, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, "", mutableListOf(), null,
            mutableListOf(Payment("", BigDecimal.ZERO, date2, PaymentMode.POS))
        ),
        RecordHolder.Expense(
            "d1", date3, BigDecimal.ZERO, BigDecimal.ZERO,BigDecimal.ZERO, "",
            null, mutableListOf(), null,
            mutableListOf(Payment("nd8hd", BigDecimal.ZERO, date3, PaymentMode.POS))
        )
    )
}