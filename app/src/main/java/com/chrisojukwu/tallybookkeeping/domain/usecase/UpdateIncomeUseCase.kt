package com.chrisojukwu.tallybookkeeping.domain.usecase

import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.domain.model.StringResponse
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import com.chrisojukwu.tallybookkeeping.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateIncomeUseCase @Inject constructor(private val repository: RecordsRepository) {

    operator fun invoke(income: RecordHolder.Income): Flow<Result<StringResponse>> =
         repository.updateIncome(income)

}