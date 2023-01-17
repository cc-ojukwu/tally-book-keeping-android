package com.chrisojukwu.tallybookkeeping.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBExpense
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBIncome
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBInventory
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(income: DBIncome)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncomeList(incomeList: List<DBIncome>)

    @Query("DELETE FROM income_table WHERE record_id = :recordId")
    suspend fun deleteIncome(recordId: String)

    @Query("DELETE FROM income_table")
    suspend fun deleteAllIncome()

    @Query("SELECT * FROM income_table")
    fun getIncomeList(): Flow<List<DBIncome>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: DBExpense)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpenseList(expenseList: List<DBExpense>)

    @Query("DELETE FROM expense_table WHERE record_id = :recordId")
    suspend fun deleteExpense(recordId: String)

    @Query("DELETE FROM expense_table")
    suspend fun deleteAllExpense()

    @Query("SELECT * FROM expense_table")
    fun getExpenseList(): Flow<List<DBExpense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryItem(inventoryItem: DBInventory)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventoryList(inventoryItemList: List<DBInventory>)

    @Query("DELETE FROM inventory_table WHERE sku = :sku")
    suspend fun deleteInventory(sku: String)

    @Query("DELETE FROM inventory_table")
    suspend fun deleteAllInventory()

    @Query("SELECT * FROM inventory_table")
    fun getInventoryList(): Flow<List<DBInventory>>


}