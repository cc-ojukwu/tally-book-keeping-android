package com.chrisojukwu.tallybookkeeping.utils

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.chrisojukwu.tallybookkeeping.domain.model.Customer
import com.chrisojukwu.tallybookkeeping.domain.model.Supplier
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkCustomer
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkSupplier
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat
import java.time.OffsetDateTime

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
    return "product-${(0..50).random()}${(0..50).random()}${(0..50).random()}}"
}

fun getRandomPaymentId(): String {
    return "payment-${(0..50).random()}${(0..50).random()}${(0..50).random()}}"
}

fun getRandomUserId(): String {
    return "user-${(0..50).random()}${(0..50).random()}${(0..50).random()}}"
}

fun getRandomInvoiceNo(): String {
    return "inv-${(0..50).random()}${(0..50).random()}${(0..50).random()}${(0..50).random()}"
}

fun String.toOffsetDateTime(): OffsetDateTime {
    return OffsetDateTime.parse(this)
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

//fun getRandomString(length: Int) : String {
//    val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
//    return (1..length)
//        .map { charset.random() }
//        .joinToString("")
//}

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