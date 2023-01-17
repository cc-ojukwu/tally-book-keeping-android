package com.chrisojukwu.tallybookkeeping.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal
import java.time.OffsetDateTime

@JsonClass(generateAdapter = true)
data class NetworkExpense(

    @Json(name="record_id")
    val recordId: String,

    @Json(name="date")
    val date: OffsetDateTime,

    @Json(name="total_amount")
    var totalAmount: BigDecimal,

    @Json(name="amount_paid")
    var amountPaid: BigDecimal,

    @Json(name="balance_due")
    var balanceDue: BigDecimal,

    @Json(name="description")
    var description: String,

    @Json(name="category")
    var category: String?,

    @Json(name="product_list")
    var productList: List<NetworkProduct>?,

    @Json(name="supplier")
    var supplier: NetworkSupplier?,

    @Json(name="payment_list")
    val paymentList: List<NetworkPayment>
)