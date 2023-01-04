package com.chrisojukwu.tallybookkeeping.data.source.local

import com.chrisojukwu.tallybookkeeping.data.source.local.dao.RecordDao
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBExpense
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBIncome
import com.chrisojukwu.tallybookkeeping.data.source.local.entity.DBInventory
import com.chrisojukwu.tallybookkeeping.di.IoDispatcher
import com.chrisojukwu.tallybookkeeping.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecordsLocalDataSourceImpl @Inject constructor(
    private val recordsDao: RecordDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RecordsLocalDataSource {

    override suspend fun insertAllIncome(incomeList: List<DBIncome>) =
        withContext(ioDispatcher) {
            recordsDao.deleteAllIncome()
            recordsDao.insertAllIncome(incomeList)
        }

    override suspend fun deleteAllIncome() =
        withContext(ioDispatcher) {
            recordsDao.deleteAllIncome()
        }

    override fun getAllIncome(): Flow<List<DBIncome>> = recordsDao.getAllIncome()

    override suspend fun insertIncome(income: DBIncome) = recordsDao.insertIncome(income)

    override suspend fun deleteIncome(income: DBIncome) = recordsDao.deleteIncome(income.recordId)

    override suspend fun insertAllExpense(expenseList: List<DBExpense>) =
        withContext(ioDispatcher) {
            recordsDao.deleteAllExpense()
            recordsDao.insertAllExpense(expenseList)
        }

    override suspend fun deleteAllExpense() =
        withContext(ioDispatcher) {
            recordsDao.deleteAllExpense()
        }

    override fun getAllExpense(): Flow<List<DBExpense>> = recordsDao.getAllExpense()

    override suspend fun insertExpense(expense: DBExpense) = recordsDao.insertExpense(expense)

    override suspend fun deleteExpense(expense: DBExpense) = recordsDao.deleteExpense(expense.recordId)

    override suspend fun insertAllInventory(stockItemList: List<DBInventory>) =
        withContext(ioDispatcher) {
            recordsDao.insertAllInventory(stockItemList)
        }

    override suspend fun deleteAllInventory() =
        withContext(ioDispatcher) {
            recordsDao.deleteAllInventory()
        }

    override fun getAllInventory(): Flow<List<DBInventory>> =
        recordsDao.getAllInventory()

    override suspend fun insertInventory(stock: DBInventory) = recordsDao.insertInventory(stock)

    override suspend fun deleteInventory(stock: DBInventory) = recordsDao.deleteInventory(stock.sku)

}