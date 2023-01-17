package com.chrisojukwu.tallybookkeeping.data.source.local

import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBExpense
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBIncome
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBInventory
import kotlinx.coroutines.flow.Flow

interface RecordsLocalDataSource {

    suspend fun insertIncomeList(incomeList: List<DBIncome>)

    suspend fun deleteAllIncome()

    fun getIncomeList(): Flow<List<DBIncome>>

    suspend fun insertIncome(income: DBIncome)

    suspend fun deleteIncome(income: DBIncome)

    suspend fun insertExpenseList(expenseList: List<DBExpense>)

    suspend fun deleteAllExpense()

    fun getExpenseList(): Flow<List<DBExpense>>

    suspend fun insertExpense(expense: DBExpense)

    suspend fun deleteExpense(expense: DBExpense)

    suspend fun insertInventoryList(inventoryList: List<DBInventory>)

    suspend fun deleteAllInventory()

    fun getInventoryList(): Flow<List<DBInventory>>

    suspend fun insertInventory(inventoryItem: DBInventory)

    suspend fun deleteInventory(inventoryItem: DBInventory)
}