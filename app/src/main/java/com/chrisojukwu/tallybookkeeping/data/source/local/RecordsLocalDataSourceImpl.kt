package com.chrisojukwu.tallybookkeeping.data.source.local

import com.chrisojukwu.tallybookkeeping.data.source.local.dao.RecordDao
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBExpense
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBIncome
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBInventory
import com.chrisojukwu.tallybookkeeping.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class RecordsLocalDataSourceImpl @Inject constructor(
    private val recordsDao: RecordDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RecordsLocalDataSource {

    override suspend fun insertIncomeList(incomeList: List<DBIncome>) =
        withContext(ioDispatcher) {
            recordsDao.deleteAllIncome()
            recordsDao.insertIncomeList(incomeList)
        }

    override suspend fun deleteAllIncome() =
        withContext(ioDispatcher) {
            recordsDao.deleteAllIncome()
        }

    override fun getIncomeList(): Flow<List<DBIncome>> = recordsDao.getIncomeList()

    override suspend fun insertIncome(income: DBIncome) = recordsDao.insertIncome(income)

    override suspend fun deleteIncome(income: DBIncome) = recordsDao.deleteIncome(income.recordId)

    override suspend fun insertExpenseList(expenseList: List<DBExpense>) =
        withContext(ioDispatcher) {
            recordsDao.deleteAllExpense()
            recordsDao.insertExpenseList(expenseList)
        }

    override suspend fun deleteAllExpense() =
        withContext(ioDispatcher) {
            recordsDao.deleteAllExpense()
        }

    override fun getExpenseList(): Flow<List<DBExpense>> = recordsDao.getExpenseList()

    override suspend fun insertExpense(expense: DBExpense) = recordsDao.insertExpense(expense)

    override suspend fun deleteExpense(expense: DBExpense) = recordsDao.deleteExpense(expense.recordId)

    override suspend fun insertInventoryList(inventoryList: List<DBInventory>) =
        withContext(ioDispatcher) {
            recordsDao.insertInventoryList(inventoryList)
        }

    override suspend fun deleteAllInventory() =
        withContext(ioDispatcher) {
            recordsDao.deleteAllInventory()
        }

    override fun getInventoryList(): Flow<List<DBInventory>> = recordsDao.getInventoryList()

    override suspend fun insertInventory(inventoryItem: DBInventory) = recordsDao.insertInventoryItem(inventoryItem)

    override suspend fun deleteInventory(inventoryItem: DBInventory) = recordsDao.deleteInventory(inventoryItem.sku)

}