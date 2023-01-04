package com.chrisojukwu.tallybookkeeping.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class NetworkProduct(
    @Json(name="id")
    val id: String,

    @Json(name="product_name")
    var productName: String,

    @Json(name="product_price")
    var productPrice: BigDecimal,

    @Json(name="product_quantity")
    var productQuantity: Int = 1,

    @Json(name="product_total_price")
    var productTotalPrice: BigDecimal
)
