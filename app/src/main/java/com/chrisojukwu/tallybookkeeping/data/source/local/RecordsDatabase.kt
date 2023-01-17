package com.chrisojukwu.tallybookkeeping.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chrisojukwu.tallybookkeeping.data.source.local.dao.RecordDao
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBExpense
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBIncome
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBInventory
import com.chrisojukwu.tallybookkeeping.utils.typeconverters.Converters

@Database(entities = [DBIncome::class, DBExpense::class, DBInventory::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RecordsDatabase : RoomDatabase() {

    abstract val recordDao: RecordDao

}