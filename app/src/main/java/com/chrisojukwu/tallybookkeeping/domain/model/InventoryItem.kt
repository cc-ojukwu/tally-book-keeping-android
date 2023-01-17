package com.chrisojukwu.tallybookkeeping.domain.model

import java.math.BigDecimal

data class InventoryItem(
    var stockName: String,
    val sku: String,
    var costPrice: BigDecimal,
    var sellingPrice: BigDecimal,
    var quantity: Int,
    var imageUrl: String?
)
