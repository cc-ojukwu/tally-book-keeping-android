package com.chrisojukwu.tallybookkeeping.data.source.remote

import com.chrisojukwu.tallybookkeeping.data.dto.NetworkExpense
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkIncome
import com.chrisojukwu.tallybookkeeping.data.dto.NetworkStockItem
import com.chrisojukwu.tallybookkeeping.domain.model.*
import com.chrisojukwu.tallybookkeeping.utils.Result

interface RecordsRemoteDataSource {

    suspend fun changePassword(changePassword: ChangePassword): Result<String?>
    suspend fun changeEmail(user: User): Result<TokenWithEmail?>
    suspend fun createNewAccount(user: User): Result<String?>
    suspend fun signInWithEmail(userInfo: SignInUser): Result<Token?>
    suspend fun signInWithGoogle(idToken: String): Result<TokenWithEmail?>
    suspend fun updateUserInfo(user: User): Result<User?>

    suspend fun getAllIncome(): Result<List<NetworkIncome>?>
    suspend fun insertIncome(income: NetworkIncome): Result<String?>
    suspend fun updateIncome(income: NetworkIncome): Result<String?>
    suspend fun deleteIncome(income: NetworkIncome): Result<String?>

    suspend fun getAllExpense(): Result<List<NetworkExpense>?>
    suspend fun insertExpense(expense: NetworkExpense): Result<String?>
    suspend fun updateExpense(expense: NetworkExpense): Result<String?>
    suspend fun deleteExpense(expense: NetworkExpense): Result<String?>

    suspend fun getAllInventory(): Result<List<NetworkStockItem>?>
    suspend fun insertInventory(stockItem: NetworkStockItem): Result<String?>
    suspend fun updateInventory(stockItem: NetworkStockItem): Result<String?>
    suspend fun deleteInventory(stockItem: NetworkStockItem): Result<String?>
}