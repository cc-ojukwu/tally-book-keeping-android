package com.chrisojukwu.tallybookkeeping.domain.model

import com.squareup.moshi.Json


data class User(
    @Json(name="email")val email: String,
    @Json(name="password")val password: String,
    @Json(name="user_id")val userId: String,
    @Json(name="provider")val provider: Provider,
    @Json(name="first_name")val firstName: String = "",
    @Json(name="last_name")val lastName: String = "",
    @Json(name="role")val role: String = "ROLE_USER",
    @Json(name="enabled")val enabled: Boolean = true
    )

