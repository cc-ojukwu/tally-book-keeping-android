package com.chrisojukwu.tallybookkeeping.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal
import java.time.OffsetDateTime

@JsonClass(generateAdapter = true)
data class NetworkIncome(
    @Json(name="record_id")
    val recordId: String,

    @Json(name="date")
    val date: OffsetDateTime,

    @Json(name="total_amount")
    val totalAmount: BigDecimal,

    @Json(name="amount_received")
    val amountReceived: BigDecimal,

    @Json(name="discount")
    val discount: BigDecimal,

    @Json(name="subtotal")
    val subTotal: BigDecimal,

    @Json(name="balance_due")
    val balanceDue: BigDecimal,

    @Json(name="description")
    val description: String,

    @Json(name="product_list")
    val productList: List<NetworkProduct>?,

    @Json(name="customer")
    val customer: NetworkCustomer?,

    @Json(name="payment_list")
    val paymentList: List<NetworkPayment>
)
