package com.chrisojukwu.tallybookkeeping.domain.model

import com.squareup.moshi.Json


data class Token(
    @Json(name="jwt-token")val jwtToken: String
)
