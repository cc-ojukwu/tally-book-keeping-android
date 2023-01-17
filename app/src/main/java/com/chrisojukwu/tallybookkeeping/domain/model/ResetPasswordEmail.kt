package com.chrisojukwu.tallybookkeeping.domain.model

import com.squareup.moshi.Json


data class ResetPasswordEmail(
    @Json(name = "email")
    val email: String
)
