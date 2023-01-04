package com.chrisojukwu.tallybookkeeping.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "inventory_table")
data class DBInventory(
    @ColumnInfo(name = "stock_name")
    var stockName: String,

    @ColumnInfo(name = "sku")
    @PrimaryKey
    val sku: String,

    @ColumnInfo(name = "cost_price")
    var costPrice: BigDecimal,

    @ColumnInfo(name = "selling_price")
    var sellingPrice: BigDecimal,

    @ColumnInfo(name = "quantity")
    var quantity: Int,

    @ColumnInfo(name = "image")
    var imageUrl: String?
)