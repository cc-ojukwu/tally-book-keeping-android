package com.chrisojukwu.tallybookkeeping.data.models

import com.google.gson.annotations.SerializedName


data class Token(
    @SerializedName("jwt-token")val jwtToken: String
)
