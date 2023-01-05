package com.chrisojukwu.tallybookkeeping.domain.repository

import com.chrisojukwu.tallybookkeeping.domain.model.*
import kotlinx.coroutines.flow.Flow
import com.chrisojukwu.tallybookkeeping.utils.Result


interface RecordsRepository {

    fun changePassword(password: ChangePassword): Flow<Result<String>>
    fun changeEmail(user: User): Flow<Result<TokenWithEmail>>
    fun createNewAccount(user: User): Flow<Result<String>>
    fun signInWithEmail(user: SignInUser): Flow<Result<Token>>
    fun signInWithGoogle(idToken: String): Flow<Result<TokenWithEmail>>
    fun updateUserInfo(user:User): Flow<Result<User>>

    fun getAllLocalIncome(): Flow<List<RecordHolder.Income>>
    fun refreshIncomeData(): Flow<Result<List<RecordHolder.Income>>>
    fun insertIncome(income: RecordHolder.Income): Flow<Result<String>>
    fun updateIncome(income: RecordHolder.Income): Flow<Result<String>>
    fun deleteIncome(income: RecordHolder.Income): Flow<Result<String>>

    fun getAllLocalExpense(): Flow<List<RecordHolder.Expense>>
    fun refreshExpenseData(): Flow<Result<List<RecordHolder.Expense>>>
    fun insertExpense(expense: RecordHolder.Expense): Flow<Result<String>>
    fun updateExpense(expense: RecordHolder.Expense): Flow<Result<String>>
    fun deleteExpense(expense: RecordHolder.Expense): Flow<Result<String>>

    fun getAllLocalInventory(): Flow<List<StockItem>>
    fun refreshInventoryData(): Flow<Result<List<StockItem>>>
    fun insertInventory(stockItem: StockItem): Flow<Result<String>>
    fun updateInventory(stockItem: StockItem): Flow<Result<String>>
    fun deleteInventory(stockItem: StockItem): Flow<Result<String>>


    //suspend fun insertAllIncome(incomeList: List<RecordHolder.Income>)
    //suspend fun deleteAllIncome()

}