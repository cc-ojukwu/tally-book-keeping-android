package com.chrisojukwu.tallybookkeeping.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkSupplier(

    @Json(name="supplier_name")
    var supplierName: String,

    @Json(name="supplier_phone")
    var supplierPhone: String
)
