package com.chrisojukwu.tallybookkeeping.utils.typeconverters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.OffsetDateTime

class OffsetDateTimeAdapter {
    @FromJson
    fun toDateTime(value: String) = OffsetDateTime.parse(value)

    @ToJson
    fun fromDateTime(value: OffsetDateTime) = value.toString()
}