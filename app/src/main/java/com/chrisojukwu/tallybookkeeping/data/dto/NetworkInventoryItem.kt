package com.chrisojukwu.tallybookkeeping.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class NetworkInventoryItem(

    @Json(name="stock_name")
    val stockName: String,

    @Json(name="sku")
    val sku: String,

    @Json(name="cost_price")
    val costPrice: BigDecimal,

    @Json(name="selling_price")
    val sellingPrice: BigDecimal,

    @Json(name="quantity")
    val quantity: Int,

    @Json(name="image_url")
    val imageUrl: String?
)

