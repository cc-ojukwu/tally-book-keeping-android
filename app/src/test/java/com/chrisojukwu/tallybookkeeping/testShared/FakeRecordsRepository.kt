package com.chrisojukwu.tallybookkeeping.testShared

import com.chrisojukwu.tallybookkeeping.domain.model.*
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import com.chrisojukwu.tallybookkeeping.utils.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRecordsRepository: RecordsRepository {
    override fun changePassword(password: OldNewPassword): Flow<Result<StringResponse>> {
        TODO("Not yet implemented")
    }

    override fun resetPassword(resetPasswordEmail: ResetPasswordEmail): Flow<Result<StringResponse>> {
        TODO("Not yet implemented")
    }

    override fun changeEmail(user: User): Flow<Result<TokenWithEmail>> {
        TODO("Not yet implemented")
    }

    override fun createNewAccount(user: User): Flow<Result<StringResponse>> = flow{
        TestData.returnedUser = user
        emit(Result.Success(StringResponse("created")))
    }

    override fun signInWithEmail(user: SignInUser): Flow<Result<Token>> {
        TODO("Not yet implemented")
    }

    override fun signInWithGoogle(idToken: String): Flow<Result<TokenWithEmail>> {
        TODO("Not yet implemented")
    }

    override fun updateUserInfo(user: User): Flow<Result<User>> = flow {
        emit(Result.Success(TestData.user))
    }

    override fun getAllLocalIncome(): Flow<List<RecordHolder.Income>> = flow {
        emit(TestData.localIncomeList)
    }

    override fun getRemoteIncomeList(): Flow<Result<List<RecordHolder.Income>>> = flow {
        emit(Result.Success(TestData.incomeList))
        //emit(Result.Error(Exception()))
    }

    override fun saveIncome(income: RecordHolder.Income): Flow<Result<StringResponse>> {
        TODO("Not yet implemented")
    }

    override fun updateIncome(income: RecordHolder.Income): Flow<Result<StringResponse>> {
        TODO("Not yet implemented")
    }

    override fun deleteIncome(income: RecordHolder.Income): Flow<Result<StringResponse>> {
        TODO("Not yet implemented")
    }

    override fun getAllLocalExpense(): Flow<List<RecordHolder.Expense>> = flow {
        emit(TestData.localExpenseList)
    }

    override fun getRemoteExpenseList(): Flow<Result<List<RecordHolder.Expense>>> = flow {
        emit(Result.Success(TestData.expenseList))
    }

    override fun saveExpense(expense: RecordHolder.Expense): Flow<Result<StringResponse>> {
        TODO("Not yet implemented")
    }

    override fun updateExpense(expense: RecordHolder.Expense): Flow<Result<StringResponse>> {
        TODO("Not yet implemented")
    }

    override fun deleteExpense(expense: RecordHolder.Expense): Flow<Result<StringResponse>> {
        TODO("Not yet implemented")
    }

    override fun getAllLocalInventory(): Flow<List<InventoryItem>> {
        TODO("Not yet implemented")
    }

    override fun getRemoteInventoryList(): Flow<Result<List<InventoryItem>>> = flow {
        emit(Result.Success(TestData.stockList))
    }

    override fun saveInventoryItem(inventoryItem: InventoryItem): Flow<Result<StringResponse>> {
        TODO("Not yet implemented")
    }

    override fun updateInventoryItem(inventoryItem: InventoryItem): Flow<Result<StringResponse>> {
        TODO("Not yet implemented")
    }

    override fun deleteInventoryItem(inventoryItem: InventoryItem): Flow<Result<StringResponse>> {
        TODO("Not yet implemented")
    }
}