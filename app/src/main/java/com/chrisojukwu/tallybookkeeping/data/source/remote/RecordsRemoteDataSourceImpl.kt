package com.chrisojukwu.tallybookkeeping.data.source.remote

import com.chrisojukwu.tallybookkeeping.data.dto.NetworkExpense
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkIncome
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkStockItem
import com.chrisojukwu.tallybookkeeping.di.IoDispatcher
import com.chrisojukwu.tallybookkeeping.domain.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import com.chrisojukwu.tallybookkeeping.utils.Result
import timber.log.Timber
import javax.inject.Inject

class RecordsRemoteDataSourceImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val apiService: ApiService
) : RecordsRemoteDataSource {

    override suspend fun changePassword(changePassword: ChangePassword): Result<String?> =
        withContext(ioDispatcher) {
            return@withContext try {
                Timber.d("calling change password apiservice")
                val result = apiService.changePassword(changePassword)
                if (result.isSuccessful) {
                    Timber.d("change password was successful")
                    Result.Success("Password changed")
                } else if (result.code() == 400 ) {
                    Timber.d("Old password is incorrect")
                    Result.Error(Exception("Old password is incorrect"))
                } else {
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Timber.d("change password exception")
                Result.Error(exception)
            }
        }

    override suspend fun changeEmail(user: User): Result<TokenWithEmail?> =
        withContext(ioDispatcher) {
            return@withContext try {
                Timber.d("calling change email apiservice")
                val result = apiService.changeEmail(user)
                if (result.isSuccessful) {
                    Timber.d("change email was successful")
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Timber.d("change email exception")
                Result.Error(exception)
            }
        }

    override suspend fun createNewAccount(user: User): Result<String?> =
        withContext(ioDispatcher) {
            return@withContext try {
                Timber.d("calling create account apiservice")
                val result = apiService.createNewUserAccount(user)
                if (result.isSuccessful) {
                    Timber.d("create account was successful")
                    Result.Success("Account created")
                } else if (result.code() == 400 ) {
                    Timber.d("email already exists")
                    Result.Error(Exception("Email already used"))
                } else {
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Timber.d("create new account exception")
                Result.Error(exception)
            }
        }

    override suspend fun signInWithEmail(userInfo: SignInUser): Result<Token?> =
        withContext(ioDispatcher) {
            return@withContext try {
                Timber.d("calling signin with email apiservice")
                val result = apiService.signInWithEmail(userInfo)
                if (result.isSuccessful) {
                    Timber.d("email sign in was successful")
                    Result.Success(result.body())
                } else if (result.code() == 400 ) {
                    Timber.d("email already exists")
                    Result.Error(Exception("Email/Password not correct"))
                } else {
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Timber.d("sign in with email exception")
                Result.Error(exception)
            }
        }

    override suspend fun signInWithGoogle(idToken: String): Result<TokenWithEmail?> =
        withContext(ioDispatcher) {
            return@withContext try {
                Timber.d("calling signin with google apiservice")
                val result = apiService.googleSignIn(idToken)
                if (result.isSuccessful) {
                    Timber.d("google sign in was successful")
                    Result.Success(result.body())
                } else {
                    Timber.d("server side error")
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Timber.d("sign in with google exception")
                Result.Error(exception)
            }
        }

    override suspend fun updateUserInfo(user: User): Result<User?> =
        withContext(ioDispatcher) {
            return@withContext try {
                Timber.d("calling update user info apiservice")
                val result = apiService.updateUserInfo(user)
                if (result.isSuccessful) {
                    Timber.d("update user info was successful")
                    Result.Success(result.body())
                } else {
                    Timber.d("server side error")
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Timber.d("update user info exception")
                Result.Error(exception)
            }
        }

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