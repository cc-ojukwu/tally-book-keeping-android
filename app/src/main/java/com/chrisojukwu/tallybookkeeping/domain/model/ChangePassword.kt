package com.chrisojukwu.tallybookkeeping.domain.model

import com.squareup.moshi.Json


data class ChangePassword(
    @Json(name = "old_password") val oldPassword: String,
    @Json(name = "new_password") val newPassword: String
)
