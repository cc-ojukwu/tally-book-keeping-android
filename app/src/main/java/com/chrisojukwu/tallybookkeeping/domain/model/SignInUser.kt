package com.chrisojukwu.tallybookkeeping.domain.model

import com.squareup.moshi.Json

data class SignInUser(
    @Json(name="email")
    val email: String,

    @Json(name="password")
    val password: String
)
