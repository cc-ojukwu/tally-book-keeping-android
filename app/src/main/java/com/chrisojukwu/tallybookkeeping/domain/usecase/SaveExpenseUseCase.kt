package com.chrisojukwu.tallybookkeeping.domain.usecase

import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.domain.model.StringResponse
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import com.chrisojukwu.tallybookkeeping.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveExpenseUseCase @Inject constructor(private val repository: RecordsRepository) {

    operator fun invoke(expense: RecordHolder.Expense): Flow<Result<StringResponse>> =
         repository.saveExpense(expense)

}