package com.chrisojukwu.tallybookkeeping.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCustomer(
    @Json(name="customer_name")
    var customerName: String,

    @Json(name="customer_phone")
    var customerPhone: String
)
