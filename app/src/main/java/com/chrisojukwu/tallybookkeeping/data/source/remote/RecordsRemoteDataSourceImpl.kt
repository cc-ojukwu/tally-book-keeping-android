package com.chrisojukwu.tallybookkeeping.data.source.remote

import com.chrisojukwu.tallybookkeeping.data.dto.NetworkExpense
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkIncome
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkStockItem
import com.chrisojukwu.tallybookkeeping.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import com.chrisojukwu.tallybookkeeping.utils.Result
import timber.log.Timber
import javax.inject.Inject

class RecordsRemoteDataSourceImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val apiService: ApiService
) : RecordsRemoteDataSource {

    override suspend fun getAllIncome(): Result<List<NetworkIncome>?> =
        withContext(ioDispatcher) {
            return@withContext try {
                Timber.d("calling get income apiservice")
                val result = apiService.getAllIncome()
                if (result.isSuccessful) {
                    val incomeList = result.body()
                    Timber.d("get income api call was successful")
                    Result.Success(incomeList)
                } else {
                    Timber.d("get income api call was not successful")
                    Result.Error(Exception("server side error"))
                }
            } catch (exception: Exception) {
                Timber.d("get income api call exception")
                Result.Error(exception)
            }
        }

    override suspend fun insertIncome(income: NetworkIncome): Result<String?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.insertIncome(income)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception(result.body()))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun updateIncome(income: NetworkIncome): Result<String?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.updateIncome(income)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception(result.body()))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }


    override suspend fun deleteIncome(income: NetworkIncome): Result<String?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.deleteIncome(income)
                if (result.isSuccessful) {
                    Result.Success(result.message())
                } else {
                    Result.Error(Exception(result.message()))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun getAllExpense(): Result<List<NetworkExpense>?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.getAllExpense()
                if (result.isSuccessful) {
                    Timber.d("get expense api call was successful")
                    val expenseList = result.body()
                    Result.Success(expenseList)
                } else {
                    Timber.d("get expense api call was not successful")
                    Result.Error(Exception("server side error"))
                }
            } catch (exception: Exception) {
                Timber.d("get expense api call exception")
                Result.Error(exception)
            }
        }




    override suspend fun insertExpense(expense: NetworkExpense): Result<String?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.insertExpense(expense)
                if (result.isSuccessful) {
                    Result.Success(result.message())
                } else {
                    Result.Error(Exception(result.message()))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun updateExpense(expense: NetworkExpense): Result<String?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.updateExpense(expense)
                if (result.isSuccessful) {
                    Result.Success(result.message())
                } else {
                    Result.Error(Exception(result.message()))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun deleteExpense(expense: NetworkExpense): Result<String?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.deleteExpense(expense)
                if (result.isSuccessful) {
                    Result.Success(result.message())
                } else {
                    Result.Error(Exception(result.message()))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun getAllInventory(): Result<List<NetworkStockItem>?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.getAllInventory()
                if (result.isSuccessful) {
                    val stockItemList = result.body()
                    Result.Success(stockItemList)
                } else {
                    Timber.d(result.message())
                    Result.Success(null)
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun insertInventory(stockItem: NetworkStockItem): Result<String?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.insertInventory(stockItem)
                if (result.isSuccessful) {
                    Result.Success(result.message())
                } else {
                    Result.Error(Exception(result.message()))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun updateInventory(stockItem: NetworkStockItem): Result<String?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.updateInventory(stockItem)
                if (result.isSuccessful) {
                    Result.Success(result.message())
                } else {
                    Result.Error(Exception(result.message()))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun deleteInventory(stockItem: NetworkStockItem): Result<String?> =
        withContext(ioDispatcher)
        {
            return@withContext try {
                val result = apiService.deleteInventory(stockItem)
                if (result.isSuccessful) {
                    Result.Success(result.message())
                } else {
                    Result.Error(Exception(result.message()))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }
}