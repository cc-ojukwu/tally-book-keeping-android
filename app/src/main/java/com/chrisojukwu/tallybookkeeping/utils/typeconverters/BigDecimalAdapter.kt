package com.chrisojukwu.tallybookkeeping.utils.typeconverters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.math.BigDecimal

class BigDecimalAdapter {
    @FromJson
    fun stringToBigDecimal(value: String): BigDecimal = BigDecimal(value)

    @ToJson
    fun bigDecimalToString(value: BigDecimal): String = value.toString()
}