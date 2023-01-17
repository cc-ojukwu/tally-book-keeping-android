package com.chrisojukwu.tallybookkeeping.data.source.remote

import com.chrisojukwu.tallybookkeeping.data.dto.NetworkExpense
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkIncome
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkInventoryItem
import com.chrisojukwu.tallybookkeeping.domain.model.*
import com.chrisojukwu.tallybookkeeping.utils.Result

interface RecordsRemoteDataSource {

    suspend fun changePassword(oldNewPassword: OldNewPassword): Result<StringResponse?>
    suspend fun resetPassword(resetPasswordEmail: ResetPasswordEmail): Result<StringResponse?>
    suspend fun changeEmail(user: User): Result<TokenWithEmail?>
    suspend fun createNewAccount(user: User): Result<StringResponse?>
    suspend fun signInWithEmail(userInfo: SignInUser): Result<Token?>
    suspend fun signInWithGoogle(idToken: String): Result<TokenWithEmail?>
    suspend fun updateUserInfo(user: User): Result<User?>
    suspend fun getUserInfo(): Result<User?>

    suspend fun getAllIncome(): Result<List<NetworkIncome>?>
    suspend fun saveIncome(income: NetworkIncome): Result<StringResponse?>
    suspend fun updateIncome(income: NetworkIncome): Result<StringResponse?>
    suspend fun deleteIncome(income: NetworkIncome): Result<StringResponse?>

    suspend fun getAllExpense(): Result<List<NetworkExpense>?>
    suspend fun saveExpense(expense: NetworkExpense): Result<StringResponse?>
    suspend fun updateExpense(expense: NetworkExpense): Result<StringResponse?>
    suspend fun deleteExpense(expense: NetworkExpense): Result<StringResponse?>

    suspend fun getAllInventory(): Result<List<NetworkInventoryItem>?>
    suspend fun saveInventoryItem(stockItem: NetworkInventoryItem): Result<StringResponse?>
    suspend fun updateInventoryItem(stockItem: NetworkInventoryItem): Result<StringResponse?>
    suspend fun deleteInventoryItem(stockItem: NetworkInventoryItem): Result<StringResponse?>
}