package com.chrisojukwu.tallybookkeeping.data.source.local

import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBExpense
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBIncome
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBInventory
import kotlinx.coroutines.flow.Flow

interface RecordsLocalDataSource {

    suspend fun insertAllIncome(incomeList: List<DBIncome>)

    suspend fun deleteAllIncome()

    fun getAllIncome(): Flow<List<DBIncome>>

    suspend fun insertIncome(income: DBIncome)
    suspend fun deleteIncome(income: DBIncome)

    suspend fun insertAllExpense(expenseList: List<DBExpense>)

    suspend fun deleteAllExpense()

    fun getAllExpense(): Flow<List<DBExpense>>

    suspend fun insertExpense(expense: DBExpense)
    suspend fun deleteExpense(expense: DBExpense)

    suspend fun insertAllInventory(stockItemList: List<DBInventory>)

    suspend fun deleteAllInventory()

    fun getAllInventory(): Flow<List<DBInventory>>

    suspend fun insertInventory(stock: DBInventory)
    suspend fun deleteInventory(stock: DBInventory)
}