package com.chrisojukwu.tallybookkeeping.domain.model


import androidx.room.ColumnInfo


data class Customer(
    @ColumnInfo(name = "customer_name")
    var customerName: String,

    @ColumnInfo(name = "customer_phone")
    var customerPhone: String
)
