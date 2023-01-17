package com.chrisojukwu.tallybookkeeping.domain.repository

import com.chrisojukwu.tallybookkeeping.domain.model.*
import kotlinx.coroutines.flow.Flow
import com.chrisojukwu.tallybookkeeping.utils.Result


interface RecordsRepository {

    fun changePassword(password: OldNewPassword): Flow<Result<StringResponse>>
    fun resetPassword(resetPasswordEmail: ResetPasswordEmail): Flow<Result<StringResponse>>
    fun changeEmail(user: User): Flow<Result<TokenWithEmail>>
    fun createNewAccount(user: User): Flow<Result<StringResponse>>
    fun signInWithEmail(user: SignInUser): Flow<Result<Token>>
    fun signInWithGoogle(idToken: String): Flow<Result<TokenWithEmail>>
    fun updateUserInfo(user:User): Flow<Result<User>>
    fun getUserInfo(): Flow<Result<User>>

    fun getAllLocalIncome(): Flow<List<RecordHolder.Income>>
    fun getRemoteIncomeList(): Flow<Result<List<RecordHolder.Income>>>
    fun saveIncome(income: RecordHolder.Income): Flow<Result<StringResponse>>
    fun updateIncome(income: RecordHolder.Income): Flow<Result<StringResponse>>
    fun deleteIncome(income: RecordHolder.Income): Flow<Result<StringResponse>>

    fun getAllLocalExpense(): Flow<List<RecordHolder.Expense>>
    fun getRemoteExpenseList(): Flow<Result<List<RecordHolder.Expense>>>
    fun saveExpense(expense: RecordHolder.Expense): Flow<Result<StringResponse>>
    fun updateExpense(expense: RecordHolder.Expense): Flow<Result<StringResponse>>
    fun deleteExpense(expense: RecordHolder.Expense): Flow<Result<StringResponse>>

    fun getAllLocalInventory(): Flow<List<InventoryItem>>
    fun getRemoteInventoryList(): Flow<Result<List<InventoryItem>>>
    fun saveInventoryItem(inventoryItem: InventoryItem): Flow<Result<StringResponse>>
    fun updateInventoryItem(inventoryItem: InventoryItem): Flow<Result<StringResponse>>
    fun deleteInventoryItem(inventoryItem: InventoryItem): Flow<Result<StringResponse>>

    fun deleteAllDatabaseData(): Flow<Result<String>>

}