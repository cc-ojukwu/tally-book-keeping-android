package com.chrisojukwu.tallybookkeeping.domain.model

import com.squareup.moshi.Json


data class Token(
    @Json(name="jwt_token")
    val jwtToken: String
)
