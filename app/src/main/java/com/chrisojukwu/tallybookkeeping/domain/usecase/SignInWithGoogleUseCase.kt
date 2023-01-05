package com.chrisojukwu.tallybookkeeping.domain.usecase

import com.chrisojukwu.tallybookkeeping.domain.model.*
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import com.chrisojukwu.tallybookkeeping.utils.Result
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor (private val repository: RecordsRepository) {

    operator fun invoke(idToken: String): Flow<Result<TokenWithEmail>> {
        return repository.signInWithGoogle(idToken)
    }
}