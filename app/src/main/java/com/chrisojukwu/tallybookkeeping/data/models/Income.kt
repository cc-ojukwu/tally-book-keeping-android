package com.chrisojukwu.tallybookkeeping.data.models

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class Income(
    var totalAmount: BigDecimal = BigDecimal.ZERO,
    var amountReceived: BigDecimal = BigDecimal.ZERO,
    var amountBalance: BigDecimal = totalAmount - amountReceived,
    var description: String = "",
    var date: LocalDate,
    var time: LocalTime,
    var category: MutableList<String>?,
    var discount: BigDecimal? = BigDecimal.ZERO,
    var customer: Customer?,
    var productList: MutableList<Product>?,
    var paymentMode: PaymentMode = PaymentMode.CASH
) {
}