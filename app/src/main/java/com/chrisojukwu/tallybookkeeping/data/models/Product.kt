package com.chrisojukwu.tallybookkeeping.data.models

import java.math.BigDecimal

data class Product(
    var productName: String,
    var productPrice: BigDecimal,
    var productQuantity: Int = 1,
    var productTotalPrice: BigDecimal = productPrice * productQuantity.toBigDecimal()
)
