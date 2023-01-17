package com.chrisojukwu.tallybookkeeping.data.source.repository

import com.chrisojukwu.tallybookkeeping.data.*
import com.chrisojukwu.tallybookkeeping.data.source.local.RecordsLocalDataSource
import com.chrisojukwu.tallybookkeeping.data.source.remote.RecordsRemoteDataSource
import com.chrisojukwu.tallybookkeeping.domain.model.*
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.chrisojukwu.tallybookkeeping.utils.Result
import timber.log.Timber


class RecordsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RecordsRemoteDataSource,
    private val localDataSource: RecordsLocalDataSource
) : RecordsRepository {

    override fun changePassword(password: OldNewPassword): Flow<Result<StringResponse>> = flow {
        when (val remoteData = remoteDataSource.changePassword(password)) {
            is Result.Success -> {
                Timber.d("repo - change password success")
                emit(Result.Success(remoteData.data!!))
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun resetPassword(resetPasswordEmail: ResetPasswordEmail): Flow<Result<StringResponse>> = flow {
        when (val remoteData = remoteDataSource.resetPassword(resetPasswordEmail)) {
            is Result.Success -> {
                Timber.d("repo - forgot password success")
                emit(Result.Success(remoteData.data!!))
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun changeEmail(user: User): Flow<Result<TokenWithEmail>> = flow {
        when (val remoteData = remoteDataSource.changeEmail(user)) {
            is Result.Success -> {
                Timber.d("repo - change email success")
                emit(Result.Success(remoteData.data!!))
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun createNewAccount(user: User): Flow<Result<StringResponse>> = flow {
        when (val remoteData = remoteDataSource.createNewAccount(user)) {
            is Result.Success -> {
                    Timber.d("repo - create account success")
                    emit(Result.Success(remoteData.data!!))
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun signInWithEmail(user: SignInUser): Flow<Result<Token>> = flow {
        when (val remoteData = remoteDataSource.signInWithEmail(user)) {
            is Result.Success -> {
                Timber.d("repo - email sign in success")
                emit(Result.Success(remoteData.data!!))
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun signInWithGoogle(idToken: String): Flow<Result<TokenWithEmail>> = flow {
        when (val remoteData = remoteDataSource.signInWithGoogle(idToken)) {
            is Result.Success -> {
                Timber.d("repo - google sign in success")
                emit(Result.Success(remoteData.data!!))
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun updateUserInfo(user: User): Flow<Result<User>> = flow {
        when (val remoteData = remoteDataSource.updateUserInfo(user)) {
            is Result.Success -> {
                Timber.d("repo - update user info success")
                emit(Result.Success(remoteData.data!!))
            }
            is Result.Error -> {
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun getUserInfo(): Flow<Result<User>> = flow {
        when (val remoteData = remoteDataSource.getUserInfo()) {
            is Result.Success -> {
                Timber.d("repo - get user info success")
                emit(Result.Success(remoteData.data!!))
            }
            is Result.Error -> {
                Timber.e(remoteData.exception)
                emit(Result.Error(remoteData.exception))
            }
            else -> {
                emit(Result.Loading)
            }
        }
    }

    override fun getAllLocalIncome(): Flow<List<RecordHolder.Income>> =
        localDataSource.getIncomeList().map {
            Timber.d("inside get local income data flow")
            it.asDomainModel()
        }


    override fun getRemoteIncomeList(): Flow<Result<List<RecordHolder.Income>>> = flow {
        when (val remoteData = remoteDataSource.getAllIncome()) {
            is Result.Success -> {
                if (remoteData.data != null) {
                    Timber.d("got non-null remote income data")
                    emit(Result.Success(remoteData.data.asDMModel()))

                    localDataSource.insertIncomeList(remoteData.data.asDBModel())
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

    override fun saveIncome(income: RecordHolder.Income): Flow<Result<StringResponse>> = flow {
        when (val remoteData = remoteDataSource.saveIncome(income.asNetworkModel())) {
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

    override fun updateIncome(income: RecordHolder.Income): Flow<Result<StringResponse>> = flow {
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

    override fun deleteIncome(income: RecordHolder.Income): Flow<Result<StringResponse>> = flow {
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
        localDataSource.getExpenseList().map {
            Timber.d("inside get local expense data flow")
            it.toDomainModel()
        }

    override fun getRemoteExpenseList(): Flow<Result<List<RecordHolder.Expense>>> = flow {
        when (val remoteData = remoteDataSource.getAllExpense()) {
            is Result.Success -> {
                if (remoteData.data != null) {
                    Timber.d("got non-null remote expense data")
                    emit(Result.Success(remoteData.data.toDMModel()))

                    localDataSource.insertExpenseList(remoteData.data.toDBModel())
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

    override fun saveExpense(expense: RecordHolder.Expense): Flow<Result<StringResponse>> = flow {
        when (val remoteData = remoteDataSource.saveExpense(expense.asNetworkModel())) {
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

    override fun updateExpense(expense: RecordHolder.Expense): Flow<Result<StringResponse>> = flow {
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

    override fun deleteExpense(expense: RecordHolder.Expense): Flow<Result<StringResponse>> = flow {
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


    override fun getAllLocalInventory(): Flow<List<InventoryItem>> =
        localDataSource.getInventoryList().map {
            it.asDomain()
        }

    override fun getRemoteInventoryList(): Flow<Result<List<InventoryItem>>> = flow {
        when (val remoteData = remoteDataSource.getAllInventory()) {
            is Result.Success -> {
                if (remoteData.data != null) {
                    Timber.d("got non-null remote inventory data")
                    emit(Result.Success(remoteData.data.toDomain()))

                    localDataSource.insertInventoryList(remoteData.data.toDB())
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

    override fun saveInventoryItem(inventoryItem: InventoryItem): Flow<Result<StringResponse>> = flow {
        when (val remoteData = remoteDataSource.saveInventoryItem(inventoryItem.toNetworkModel())) {
            is Result.Success -> {
                Timber.d("insert inventory success")
                emit(Result.Success(remoteData.data!!))

                localDataSource.insertInventory(inventoryItem.toDBModel())
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

    override fun updateInventoryItem(inventoryItem: InventoryItem): Flow<Result<StringResponse>> = flow {
        when (val remoteData = remoteDataSource.updateInventoryItem(inventoryItem.toNetworkModel())) {
            is Result.Success -> {
                Timber.d("update inventory success")
                emit(Result.Success(remoteData.data!!))

                localDataSource.insertInventory(inventoryItem.toDBModel())
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

    override fun deleteInventoryItem(inventoryItem: InventoryItem): Flow<Result<StringResponse>> = flow {
        when (val remoteData = remoteDataSource.deleteInventoryItem(inventoryItem.toNetworkModel())) {
            is Result.Success -> {
                Timber.d("delete inventory success")
                emit(Result.Success(remoteData.data!!))

                localDataSource.deleteInventory(inventoryItem.toDBModel())
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

    override fun deleteAllDatabaseData(): Flow<Result<String>> = flow {
        try {
            Timber.e("repo - starting database wipe ")
            localDataSource.deleteAllIncome()
            localDataSource.deleteAllExpense()
            localDataSource.deleteAllInventory()
            emit(Result.Success("success"))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }


}