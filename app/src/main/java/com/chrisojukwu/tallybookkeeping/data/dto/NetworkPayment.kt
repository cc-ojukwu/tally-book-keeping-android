package com.chrisojukwu.tallybookkeeping.data.dto

import com.chrisojukwu.tallybookkeeping.domain.model.PaymentMode
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal
import java.time.OffsetDateTime

@JsonClass(generateAdapter = true)
data class NetworkPayment(
    @Json(name="id")
    val id: String,

    @Json(name="payment_amount")
    var paymentAmount: BigDecimal,

    @Json(name="payment_date")
    var paymentDate: OffsetDateTime,

    @Json(name="payment_mode")
    var paymentMode: PaymentMode
)
