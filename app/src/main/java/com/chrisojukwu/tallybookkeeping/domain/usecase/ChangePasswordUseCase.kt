package com.chrisojukwu.tallybookkeeping.domain.usecase

import com.chrisojukwu.tallybookkeeping.domain.model.OldNewPassword
import com.chrisojukwu.tallybookkeeping.domain.model.StringResponse
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import com.chrisojukwu.tallybookkeeping.utils.Result
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor (private val repository: RecordsRepository) {

    operator fun invoke(oldNewPassword: OldNewPassword): Flow<Result<StringResponse>> {
        return repository.changePassword(oldNewPassword)
    }
}