package com.chrisojukwu.tallybookkeeping.domain.usecase

import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import com.chrisojukwu.tallybookkeeping.utils.Result
import javax.inject.Inject

class GetExpenseListUseCase @Inject constructor(private val repository: RecordsRepository) {

    operator fun invoke(): Flow<Result<List<RecordHolder.Expense>>> {
        return repository.getRemoteExpenseList()
    }
}