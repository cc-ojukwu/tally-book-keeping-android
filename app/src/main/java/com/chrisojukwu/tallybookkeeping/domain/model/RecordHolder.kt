package com.chrisojukwu.tallybookkeeping.domain.model

import java.math.BigDecimal
import java.time.OffsetDateTime

sealed class RecordHolder() {

    abstract var date: OffsetDateTime
    abstract val recordId: String

    data class Income(
        override val recordId: String,
        override var date: OffsetDateTime,
        var totalAmount: BigDecimal = BigDecimal.ZERO,
        var amountReceived: BigDecimal = BigDecimal.ZERO,
        var discount: BigDecimal = BigDecimal.ZERO,
        var subTotal: BigDecimal = BigDecimal.ZERO,
        var balanceDue: BigDecimal = BigDecimal.ZERO,
        var description: String,
        var productList: MutableList<Product>? = mutableListOf(),
        var customer: Customer?,
        var paymentList: MutableList<Payment>
    ) : RecordHolder()


    data class Expense(
        override val recordId: String,
        override var date: OffsetDateTime,
        var totalAmount: BigDecimal = BigDecimal.ZERO,
        var amountPaid: BigDecimal = BigDecimal.ZERO,
        var balanceDue: BigDecimal = BigDecimal.ZERO,
        var description: String,
        var category: String?,
        var productList: MutableList<Product>? = mutableListOf(),
        var supplier: Supplier?,
        var paymentList: MutableList<Payment>
    ) : RecordHolder()

    data class Header(
        override val recordId: String,
        override var date: OffsetDateTime,
        var incomeTotal: BigDecimal = BigDecimal.ZERO,
        var expenseTotal: BigDecimal = BigDecimal.ZERO,
        var balance: BigDecimal = BigDecimal.ZERO
    ) : RecordHolder()
}
