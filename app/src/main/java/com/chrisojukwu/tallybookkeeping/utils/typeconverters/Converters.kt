package com.chrisojukwu.tallybookkeeping.utils.typeconverters

import androidx.room.TypeConverter
import com.chrisojukwu.tallybookkeeping.domain.model.Payment
import com.chrisojukwu.tallybookkeeping.domain.model.Product
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.math.BigDecimal
import java.time.OffsetDateTime


class Converters {

    private val moshi: Moshi = Moshi.Builder()
        .add(BigDecimalAdapter())
        .add(OffsetDateTimeAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()



    @TypeConverter
    fun fromProductListToJson(list: List<Product>?): String? {
        val type = Types.newParameterizedType(List::class.java, Product::class.java)
        val adapter = moshi.adapter<List<Product>>(type)
        return adapter.toJson(list)

    }

    @TypeConverter
    fun fromJsonToProductList(json: String): List<Product>? {
        val type = Types.newParameterizedType(List::class.java, Product::class.java)
        val adapter = moshi.adapter<List<Product>>(type)
        return adapter.fromJson(json)
    }

    @TypeConverter
    //@ToJson
    fun fromPaymentListToJson(list: List<Payment>): String? {
        val type = Types.newParameterizedType(List::class.java, Payment::class.java)
        val adapter = moshi.adapter<List<Payment>>(type)
        return adapter.toJson(list)
    }

    @TypeConverter
    fun fromJsonToPaymentList(json: String): List<Payment>? {
        val type = Types.newParameterizedType(List::class.java, Payment::class.java)
        val adapter = moshi.adapter<List<Payment>>(type)
        return adapter.fromJson(json)
    }

    @TypeConverter
    fun dateFromString(value: String): OffsetDateTime? =
        try {
            OffsetDateTime.parse(value)
        } catch (e: Exception) {
            null
        }

    @TypeConverter
    fun dateToString(value: OffsetDateTime): String? =
        try {
            value.toString()
        } catch (e: Exception) {
            null
        }

    @TypeConverter
    fun bigDecimalToString(value: BigDecimal): String? =
        try {
            value.toString()
        } catch (e: Exception) {
            null
        }

    @TypeConverter
    fun stringToBigDecimal(value: String): BigDecimal? =
        try {
            BigDecimal(value)
        } catch (e: Exception) {
            null
        }
}
