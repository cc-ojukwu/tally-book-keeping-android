package com.chrisojukwu.tallybookkeeping.data.source.repository

import com.chrisojukwu.tallybookkeeping.data.*
import com.chrisojukwu.tallybookkeeping.data.source.local.RecordsLocalDataSource
import com.chrisojukwu.tallybookkeeping.data.source.remote.RecordsRemoteDataSource
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.domain.model.StockItem
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.chrisojukwu.tallybookkeeping.utils.Result
import timber.log.Timber


class RecordsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RecordsRemoteDataSource,
    private val localDataSource: RecordsLocalDataSource
) : RecordsRepository {

//    Prevents multiple consumers requesting data at the same time
//    private val loadDataLock = Any()

    override fun getAllLocalIncome(): Flow<List<RecordHolder.Income>> =
        localDataSource.getAllIncome().map {
            Timber.d("inside get local income data flow")
            it.asDomainModel()
        }


    override fun refreshIncomeData(): Flow<Result<List<RecordHolder.Income>>> = flow {
        when (val remoteData = remoteDataSource.getAllIncome()) {
            is Result.Success -> {
                if (remoteData.data != null) {
                    Timber.d("got non-null remote income data")
                    emit(Result.Success(remoteData.data.asDMModel()))

                    localDataSource.insertAllIncome(remoteData.data.asDBModel())
                    Timber.d("inserted remote income data into database")
                }
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun insertIncome(income: RecordHolder.Income): Flow<Result<String>> = flow {
        when (val remoteData = remoteDataSource.insertIncome(income.asNetworkModel())) {
            is Result.Success -> {
                    Timber.d("insert income success")
                    emit(Result.Success(remoteData.data!!))

                    localDataSource.insertIncome(income.asDBModel())
                    Timber.d("inserted income into database")
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun updateIncome(income: RecordHolder.Income): Flow<Result<String>> = flow {
        when (val remoteData = remoteDataSource.updateIncome(income.asNetworkModel())) {
            is Result.Success -> {
                Timber.d("update income success")
                emit(Result.Success(remoteData.data!!))

                localDataSource.insertIncome(income.asDBModel())
                Timber.d("updated income in database")
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun deleteIncome(income: RecordHolder.Income): Flow<Result<String>> = flow {
        when (val remoteData = remoteDataSource.deleteIncome(income.asNetworkModel())) {
            is Result.Success -> {
                Timber.d("delete income success")
                emit(Result.Success(remoteData.data!!))

                localDataSource.deleteIncome(income.asDBModel())
                Timber.d("deleted income from database")
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }




    override fun getAllLocalExpense(): Flow<List<RecordHolder.Expense>> =
        localDataSource.getAllExpense().map {
            Timber.d("inside get local expense data flow")
            it.toDomainModel()
        }

    override fun refreshExpenseData(): Flow<Result<List<RecordHolder.Expense>>> = flow {
        when (val remoteData = remoteDataSource.getAllExpense()) {
            is Result.Success -> {
                if (remoteData.data != null) {
                    Timber.d("got non-null remote expense data")
                    emit(Result.Success(remoteData.data.toDMModel()))

                    localDataSource.insertAllExpense(remoteData.data.toDBModel())
                    Timber.d("inserted remote expense data into database")
                }
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun insertExpense(expense: RecordHolder.Expense): Flow<Result<String>> = flow {
        when (val remoteData = remoteDataSource.insertExpense(expense.asNetworkModel())) {
            is Result.Success -> {
                Timber.d("insert expense success")
                emit(Result.Success(remoteData.data!!))

                localDataSource.insertExpense(expense.asDBModel())
                Timber.d("inserted expense into database")
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun updateExpense(expense: RecordHolder.Expense): Flow<Result<String>> = flow {
        when (val remoteData = remoteDataSource.updateExpense(expense.asNetworkModel())) {
            is Result.Success -> {
                Timber.d("update expense success")
                emit(Result.Success(remoteData.data!!))

                localDataSource.insertExpense(expense.asDBModel())
                Timber.d("updated expense in database")
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun deleteExpense(expense: RecordHolder.Expense): Flow<Result<String>> = flow {
        when (val remoteData = remoteDataSource.deleteExpense(expense.asNetworkModel())) {
            is Result.Success -> {
                Timber.d("delete expense success")
                emit(Result.Success(remoteData.data!!))

                localDataSource.deleteExpense(expense.asDBModel())
                Timber.d("deleted expense from database")
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }


    override fun getAllLocalInventory(): Flow<List<StockItem>> =
        localDataSource.getAllInventory().map {
            it.asDomain()
        }

    override fun refreshInventoryData(): Flow<Result<List<StockItem>>> = flow {
        when (val remoteData = remoteDataSource.getAllInventory()) {
            is Result.Success -> {
                if (remoteData.data != null) {
                    Timber.d("got non-null remote inventory data")
                    emit(Result.Success(remoteData.data.toDomain()))

                    localDataSource.insertAllInventory(remoteData.data.toDB())
                    Timber.d("inserted remote inventory data into database")
                }
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun insertInventory(stockItem: StockItem): Flow<Result<String>> = flow {
        when (val remoteData = remoteDataSource.insertInventory(stockItem.toNetworkModel())) {
            is Result.Success -> {
                Timber.d("insert inventory success")
                emit(Result.Success(remoteData.data!!))

                localDataSource.insertInventory(stockItem.toDBModel())
                Timber.d("inserted inventory into database")
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun updateInventory(stockItem: StockItem): Flow<Result<String>> = flow {
        when (val remoteData = remoteDataSource.updateInventory(stockItem.toNetworkModel())) {
            is Result.Success -> {
                Timber.d("update inventory success")
                emit(Result.Success(remoteData.data!!))

                localDataSource.insertInventory(stockItem.toDBModel())
                Timber.d("updated inventory in database")
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun deleteInventory(stockItem: StockItem): Flow<Result<String>> = flow {
        when (val remoteData = remoteDataSource.deleteInventory(stockItem.toNetworkModel())) {
            is Result.Success -> {
                Timber.d("delete inventory success")
                emit(Result.Success(remoteData.data!!))

                localDataSource.deleteInventory(stockItem.toDBModel())
                Timber.d("deleted inventory from database")
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }


}