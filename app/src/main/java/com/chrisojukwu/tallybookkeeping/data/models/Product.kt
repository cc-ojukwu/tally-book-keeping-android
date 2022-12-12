package com.chrisojukwu.tallybookkeeping.data.models

import android.os.Parcelable
import java.math.BigDecimal

data class Product(
    val id: String,
    var productName: String = "",
    var productPrice: BigDecimal = BigDecimal.ZERO,
    var productQuantity: Int = 1,
    var productTotalPrice: BigDecimal = BigDecimal.ZERO
)
