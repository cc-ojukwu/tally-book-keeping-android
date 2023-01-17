package com.chrisojukwu.tallybookkeeping.utils

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkCustomer
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkSupplier
import com.chrisojukwu.tallybookkeeping.domain.model.Customer
import com.chrisojukwu.tallybookkeeping.domain.model.Supplier
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

fun View.setupMaxHeight(maxHeight: Double = 0.8) {
    val displayMetrics = context?.resources?.displayMetrics
    val height = displayMetrics?.heightPixels
    val maximalHeight = (height?.times(maxHeight))?.toInt()
    val layoutParams = this.layoutParams
    maximalHeight?.let {
        layoutParams.height = it
    }
    this.layoutParams = layoutParams
}

fun String.checkIfValidNumber(): Boolean {
    return try {
        this.toDouble() > 0.0
    } catch (e: Exception) {
        false
    }
}


fun getRandomSKU(): String {
    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
    val random = (1..6)
        .map { charset.random() }
        .joinToString("")
    return "sku-$random"
}

fun getRandomRecordId(): String {
    return "record${(0..50).random()}${(0..50).random()}${(0..50).random()}${(0..50).random()}"
}

fun getRandomProductId(): String {
    return "product-${(0..50).random()}${(0..50).random()}${(0..50).random()}"
}

fun getRandomPaymentId(): String {
    return "payment-${(0..50).random()}${(0..50).random()}${(0..50).random()}"
}

fun getRandomUserId(): String {
    return "user-${(0..50).random()}${(0..50).random()}${(0..50).random()}"
}

fun NetworkCustomer.toCustomer(): Customer {
    return Customer(this.customerName, this.customerPhone)
}

fun Customer.toNetworkCustomer(): NetworkCustomer {
    return NetworkCustomer(this.customerName, this.customerPhone)
}

fun NetworkSupplier.toSupplier(): Supplier {
    return Supplier(this.supplierName, this.supplierPhone)
}

fun Supplier.toNetworkSupplier(): NetworkSupplier {
    return NetworkSupplier(this.supplierName, this.supplierPhone)
}

fun BigDecimal.toTwoDP(): BigDecimal {
    val numberFormat = NumberFormat.getNumberInstance()
    numberFormat.minimumFractionDigits = 2
    numberFormat.maximumFractionDigits = 2
    numberFormat.roundingMode = RoundingMode.DOWN

    return numberFormat.format(this).toBigDecimal()
}

fun verifyNumberWithDecimal(string: String, maxBeforePoint: Int = 9, maxDecimal: Int = 2): String {
    var input = string
    if (input[0] == '.') input = "0$input"
    val max = input.length
    var rFinal = ""
    var after = false
    var i = 0
    var up = 0
    var decimal = 0
    var t: Char
    while (i < max) {
        t = input[i]
        if (t != '.' && !after) {
            up++
            if (up > maxBeforePoint) return rFinal
        } else if (t == '.') {
            after = true
        } else {
            decimal++
            if (decimal > maxDecimal) return rFinal
        }
        rFinal = rFinal + t
        i++
    }
    return rFinal
}

fun formatDateToString(offsetDateTime: OffsetDateTime) : MutableLiveData<String> {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return MutableLiveData(offsetDateTime.toLocalDate().format(formatter))
}

/*
/**
 * Truncate long text with a preference for word boundaries and without trailing punctuation.
 */
fun String.smartTruncate(length: Int): String {
    val words = split(" ")
    var added = 0
    var hasMore = false
    val builder = StringBuilder()
    for (word in words) {
        if (builder.length > length) {
            hasMore = true
            break
        }
        builder.append(word)
        builder.append(" ")
        added += 1
    }

    PUNCTUATION.map {
        if (builder.endsWith(it)) {
            builder.replace(builder.length - it.length, builder.length, "")
        }
    }

    if (hasMore) {
        builder.append("...")
    }
    return builder.toString()
}
 */