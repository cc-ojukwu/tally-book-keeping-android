package com.chrisojukwu.tallybookkeeping.domain.usecase

import com.chrisojukwu.tallybookkeeping.domain.model.SignInUser
import com.chrisojukwu.tallybookkeeping.domain.model.Token
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import com.chrisojukwu.tallybookkeeping.utils.Result
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor (private val repository: RecordsRepository) {

    operator fun invoke(user: SignInUser): Flow<Result<Token>> {
        return repository.signInWithEmail(user)
    }
}