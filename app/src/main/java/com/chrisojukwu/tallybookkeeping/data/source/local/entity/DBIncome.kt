package com.chrisojukwu.tallybookkeeping.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chrisojukwu.tallybookkeeping.domain.model.Customer
import com.chrisojukwu.tallybookkeeping.domain.model.Payment
import com.chrisojukwu.tallybookkeeping.domain.model.Product
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity(tableName = "income_table")
data class DBIncome(
    @ColumnInfo(name = "record_id")
    @PrimaryKey
    val recordId: String,

    @ColumnInfo(name = "date")
    var date: OffsetDateTime,

    @ColumnInfo(name = "total_amount")
    var totalAmount: BigDecimal,

    @ColumnInfo(name = "amount_received")
    var amountReceived: BigDecimal,

    @ColumnInfo(name = "discount")
    var discount: BigDecimal,

    @ColumnInfo(name = "subtotal")
    var subTotal: BigDecimal,

    @ColumnInfo(name = "balance_due")
    var balanceDue: BigDecimal,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "product_list")
    var productList: MutableList<Product>?,

    @Embedded
    var customer: Customer?,

    @ColumnInfo(name = "payment_list")
    var paymentList: MutableList<Payment>
)
