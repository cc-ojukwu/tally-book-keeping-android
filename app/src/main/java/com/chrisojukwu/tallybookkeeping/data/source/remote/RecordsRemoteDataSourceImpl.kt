package com.chrisojukwu.tallybookkeeping.data.source.remote

import com.chrisojukwu.tallybookkeeping.data.dto.NetworkExpense
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkIncome
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkInventoryItem
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

    override suspend fun changePassword(oldNewPassword: OldNewPassword): Result<StringResponse?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.changePassword(oldNewPassword)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else if (result.code() == 400 ) {
                    Result.Error(Exception("Old password is incorrect"))
                } else {
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun resetPassword(resetPasswordEmail: ResetPasswordEmail): Result<StringResponse?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.resetPassword(resetPasswordEmail)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }


    override suspend fun changeEmail(user: User): Result<TokenWithEmail?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.changeEmail(user)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun createNewAccount(user: User): Result<StringResponse?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.createNewUserAccount(user)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else if (result.code() == 400 ) {
                    Result.Error(Exception("Email already used"))
                } else {
                    Result.Error(Exception(result.body()?.response))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun signInWithEmail(userInfo: SignInUser): Result<Token?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.signInWithEmail(userInfo)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else if (result.code() == 400 ) {
                    Result.Error(Exception("Email/Password not correct"))
                } else {
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun signInWithGoogle(idToken: String): Result<TokenWithEmail?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.googleSignIn(idToken)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun updateUserInfo(user: User): Result<User?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.updateUserInfo(user)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun getUserInfo(): Result<User?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.getUserInfo()
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception("Error, try again later"))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun getAllIncome(): Result<List<NetworkIncome>?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.getAllIncome()
                if (result.isSuccessful) {
                    val incomeList = result.body()
                    Result.Success(incomeList)
                } else {
                    Result.Error(Exception("server side error"))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun saveIncome(income: NetworkIncome): Result<StringResponse?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.insertIncome(income)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception(result.body()?.response))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun updateIncome(income: NetworkIncome): Result<StringResponse?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.updateIncome(income)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception(result.body()?.response))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }


    override suspend fun deleteIncome(income: NetworkIncome): Result<StringResponse?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.deleteIncome(income)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception(result.body()?.response))
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
                    val expenseList = result.body()
                    Result.Success(expenseList)
                } else {
                    Result.Error(Exception("server side error"))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }




    override suspend fun saveExpense(expense: NetworkExpense): Result<StringResponse?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.insertExpense(expense)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception(result.body()?.response))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun updateExpense(expense: NetworkExpense): Result<StringResponse?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.updateExpense(expense)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception(result.body()?.response))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun deleteExpense(expense: NetworkExpense): Result<StringResponse?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.deleteExpense(expense)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception(result.body()?.response))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun getAllInventory(): Result<List<NetworkInventoryItem>?> =
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

    override suspend fun saveInventoryItem(stockItem: NetworkInventoryItem): Result<StringResponse?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.saveInventoryItem(stockItem)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception(result.body()?.response))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun updateInventoryItem(stockItem: NetworkInventoryItem): Result<StringResponse?> =
        withContext(ioDispatcher) {
            return@withContext try {
                val result = apiService.updateInventoryItem(stockItem)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception(result.body()?.response))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }

    override suspend fun deleteInventoryItem(stockItem: NetworkInventoryItem): Result<StringResponse?> =
        withContext(ioDispatcher)
        {
            return@withContext try {
                val result = apiService.deleteInventoryItem(stockItem)
                if (result.isSuccessful) {
                    Result.Success(result.body())
                } else {
                    Result.Error(Exception(result.body()?.response))
                }
            } catch (exception: Exception) {
                Result.Error(exception)
            }
        }
}