package com.chrisojukwu.tallybookkeeping.domain.usecase

import com.chrisojukwu.tallybookkeeping.domain.model.StringResponse
import com.chrisojukwu.tallybookkeeping.domain.model.User
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import com.chrisojukwu.tallybookkeeping.utils.Result
import javax.inject.Inject

class CreateNewAccountUseCase @Inject constructor (private val repository: RecordsRepository) {

    operator fun invoke(user: User): Flow<Result<StringResponse>> {
        return repository.createNewAccount(user)
    }
}