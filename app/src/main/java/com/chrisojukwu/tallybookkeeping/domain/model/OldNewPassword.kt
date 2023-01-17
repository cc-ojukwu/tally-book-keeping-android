package com.chrisojukwu.tallybookkeeping.domain.model

import com.squareup.moshi.Json


data class OldNewPassword(
    @Json(name = "old_password")
    val oldPassword: String,

    @Json(name = "new_password")
    val newPassword: String
)
