package com.chrisojukwu.tallybookkeeping.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import com.chrisojukwu.tallybookkeeping.di.IoDispatcher
import com.chrisojukwu.tallybookkeeping.domain.model.ChangePassword
import com.chrisojukwu.tallybookkeeping.domain.model.TokenWithEmail
import com.chrisojukwu.tallybookkeeping.domain.model.User
import com.chrisojukwu.tallybookkeeping.domain.usecase.ChangeEmailUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.ChangePasswordUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.UpdateUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import com.chrisojukwu.tallybookkeeping.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val changeEmailUseCase: ChangeEmailUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _accountEmail = MutableLiveData<String>()
    val accountEmail: LiveData<String> = _accountEmail

    private val _accountFirstName = MutableLiveData<String>()
    val accountFirstName: LiveData<String> = _accountFirstName

    private val _accountLastName = MutableLiveData<String>()
    val accountLastName: LiveData<String> = _accountLastName

    private val _businessName = MutableLiveData<String>()
    val businessName: LiveData<String> = _businessName

    private val _businessAddress = MutableLiveData<String>()
    val businessAddress: LiveData<String> = _businessAddress

    private val _businessPhone = MutableLiveData<String>()
    val businessPhone: LiveData<String> = _businessPhone

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch (ioDispatcher) {
            preferenceStorage.getUserEmail.collect {
                _accountEmail.value = it
            }
            preferenceStorage.getUserFirstName.collect {
                _accountFirstName.value = it
            }
            preferenceStorage.getUserLastName.collect {
                _accountLastName.value = it
            }
            preferenceStorage.getBusinessName.collect {
                _businessName.value = it
            }
            preferenceStorage.getBusinessAddress.collect {
                _businessAddress.value = it
            }
            preferenceStorage.getBusinessPhone.collect {
                _businessPhone.value = it
            }
        }
    }

    fun changeEmail(user: User): Flow<Result<TokenWithEmail>> =
        changeEmailUseCase(user)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        preferenceStorage.saveUserEmail(result.data.email)
                        preferenceStorage.saveToken(result.data.jwtToken)
                    }
                    else -> {}
                }
            }

    fun updateUserInfo(user: User): Flow<Result<User>> =
        updateUserInfoUseCase(user)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        preferenceStorage.saveUserFirstName(result.data.firstName)
                        preferenceStorage.saveUserLastName(result.data.lastName)
                        preferenceStorage.saveBusinessName(result.data.businessName)
                        preferenceStorage.saveBusinessAddress(result.data.businessAddress)
                        preferenceStorage.saveBusinessPhone(result.data.businessPhone)
                    }
                    else -> {}
                }
            }

    fun changePassword(password: ChangePassword): Flow<Result<String>> =
        changePasswordUseCase(password)

    suspend fun signOut(): Boolean =
        withContext(ioDispatcher) {
            preferenceStorage.clearDatastore()
            return@withContext true
        }



}