package com.chrisojukwu.tallybookkeeping.domain.model

import com.squareup.moshi.Json


data class StringResponse(
    @Json(name="response")
    val response: String
)
