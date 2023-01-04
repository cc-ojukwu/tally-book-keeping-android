package com.chrisojukwu.tallybookkeeping.utils

import androidx.lifecycle.MutableLiveData
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun formatNumber(number: BigDecimal): MutableLiveData<String> {
    //val locale = Locale("fr", "FR")
    //val decFormat = DecimalFormat("###,###,##0.00")
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.isGroupingUsed = true
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = 2
    //decFormat.groupingSize = 3
    //val value = decFormat.format(number)
    return MutableLiveData(numberFormat.format(number))
}

fun BigDecimal.formatAsCurrency() : String {
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.isGroupingUsed = true
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = 2
    return numberFormat.format(this)
}

fun formatDateToString(offsetDateTime: OffsetDateTime) : MutableLiveData<String> {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return MutableLiveData(offsetDateTime.toLocalDate().format(formatter))
}