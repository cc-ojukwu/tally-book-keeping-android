package com.chrisojukwu.tallybookkeeping.domain.usecase

import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalIncomeListUseCase @Inject constructor (private val repository: RecordsRepository) {

    operator fun invoke(): Flow<List<RecordHolder.Income>> {
        return repository.getAllLocalIncome()
    }
}