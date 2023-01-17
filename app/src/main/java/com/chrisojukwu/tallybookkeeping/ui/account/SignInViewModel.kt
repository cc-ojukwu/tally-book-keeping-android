package com.chrisojukwu.tallybookkeeping.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import com.chrisojukwu.tallybookkeeping.domain.model.*
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.chrisojukwu.tallybookkeeping.domain.usecase.CreateNewAccountUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.ResetPasswordUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.SignInWithEmailUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val createNewAccountUseCase: CreateNewAccountUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun createAccount(user: User): Flow<Result<StringResponse>> =
        createNewAccountUseCase(user)

    fun signInWithEmail(user: SignInUser): Flow<Result<Token>> =
        signInWithEmailUseCase(user)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        preferenceStorage.saveUserEmail(user.email)
                        preferenceStorage.saveToken(result.data.jwtToken)
                        preferenceStorage.userSignInStatus(true)
                    }
                    else -> {}
                }
            }

    fun signInWithGoogle(idToken: String): Flow<Result<TokenWithEmail>> =
        signInWithGoogleUseCase(idToken)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        preferenceStorage.saveUserEmail(result.data.email)
                        preferenceStorage.saveToken(result.data.jwtToken)
                        preferenceStorage.userSignInStatus(true)
                    }
                    else -> {}
                }
            }

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    fun resetPassword(resetPasswordEmail: ResetPasswordEmail): Flow<Result<StringResponse>> =
        resetPasswordUseCase(resetPasswordEmail)

}