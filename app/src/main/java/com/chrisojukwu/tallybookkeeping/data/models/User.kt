package com.chrisojukwu.tallybookkeeping.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("email")val email: String,
    @SerializedName("password")val password: String,
    @SerializedName("user_id")val userId: String,
    @SerializedName("provider")val provider: Provider,
    @SerializedName("first_name")val firstName: String = "",
    @SerializedName("last_name")val lastName: String = "",
    @SerializedName("role")val role: String = "ROLE_USER",
    @SerializedName("enabled")val enabled: Boolean = true
    )

