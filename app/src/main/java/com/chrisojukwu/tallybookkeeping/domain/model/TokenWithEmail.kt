package com.chrisojukwu.tallybookkeeping.domain.model

import com.squareup.moshi.Json


data class TokenWithEmail(
    @Json(name="jwt-token")val jwtToken: String,
    @Json(name="email")val email: String,
)
