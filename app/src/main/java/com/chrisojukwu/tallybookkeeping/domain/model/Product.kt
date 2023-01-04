package com.chrisojukwu.tallybookkeeping.domain.model

import com.squareup.moshi.Json
import java.math.BigDecimal


data class Product(
    @Json(name="id")
    val id: String,

    @Json(name="product_name")
    var productName: String,

    @Json(name="product_price")
    var productPrice: BigDecimal = BigDecimal.ZERO,

    @Json(name="product_quantity")
    var productQuantity: Int = 1,

    @Json(name="product_total_price")
    var productTotalPrice: BigDecimal = BigDecimal.ZERO
)

