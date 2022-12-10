package com.chrisojukwu.tallybookkeeping.data.models

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

sealed class RecordHolder() {

    abstract var date: LocalDateTime
    abstract val recordId: String

    data class Income(
        override val recordId: String,
        override var date: LocalDateTime = LocalDateTime.now(),
        var totalAmount: BigDecimal = BigDecimal.ZERO,
        var amountReceived: BigDecimal = BigDecimal.ZERO,
        var discount: BigDecimal = BigDecimal.ZERO,
        var subTotal: BigDecimal = BigDecimal.ZERO,
        var balanceDue: BigDecimal = BigDecimal.ZERO,
        var description: String = "",
        var productList: MutableList<Product>? = mutableListOf(),
        var customer: Customer? = Customer("", ""),
        var paymentMode: PaymentMode = PaymentMode.CASH,
    ) : RecordHolder()

    data class Expense(
        override val recordId: String,
        override var date: LocalDateTime = LocalDateTime.now(),
        var totalAmount: BigDecimal = BigDecimal.ZERO,
        var amountPaid: BigDecimal = BigDecimal.ZERO,
        var balanceDue: BigDecimal = BigDecimal.ZERO,
        var description: String = "",
        var category: String? = "",
        var productList: MutableList<Product>? = mutableListOf(),
        var supplier: Supplier? = Supplier("", ""),
        var paymentMode: PaymentMode = PaymentMode.CASH
    ) : RecordHolder()

    data class Header(
        override val recordId: String,
        override var date: LocalDateTime = LocalDateTime.now(),
        var incomeTotal: BigDecimal = BigDecimal.ZERO,
        var expenseTotal: BigDecimal = BigDecimal.ZERO,
        var balance: BigDecimal = BigDecimal.ZERO
    ) : RecordHolder()
}
