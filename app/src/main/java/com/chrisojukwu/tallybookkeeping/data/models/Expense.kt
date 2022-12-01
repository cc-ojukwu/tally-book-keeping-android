package com.chrisojukwu.tallybookkeeping.data.models

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

class Expense(
    var totalAmount: BigDecimal,
    var amountPaid: BigDecimal,
    var amountBalance: BigDecimal = totalAmount - amountPaid,
    var description: String,
    var date: LocalDate,
    var time: LocalTime,
    var category: MutableList<String>?,
    var productList: MutableList<Product>?
) {

}