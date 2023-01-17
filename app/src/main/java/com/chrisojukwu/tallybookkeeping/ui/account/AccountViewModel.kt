package com.chrisojukwu.tallybookkeeping.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import com.chrisojukwu.tallybookkeeping.di.IoDispatcher
import com.chrisojukwu.tallybookkeeping.domain.model.OldNewPassword
import com.chrisojukwu.tallybookkeeping.domain.model.StringResponse
import com.chrisojukwu.tallybookkeeping.domain.model.TokenWithEmail
import com.chrisojukwu.tallybookkeeping.domain.model.User
import com.chrisojukwu.tallybookkeeping.domain.usecase.ChangeEmailUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.ChangePasswordUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.DeleteAllDatabaseDataUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.UpdateUserInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import com.chrisojukwu.tallybookkeeping.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val changeEmailUseCase: ChangeEmailUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val deleteAllDatabaseDataUseCase: DeleteAllDatabaseDataUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

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
        getUserInfoFromPreferences()
    }

    private fun getUserInfoFromPreferences() {
        Timber.d("trying to get user details")
        viewModelScope.launch(ioDispatcher) {
            preferenceStorage.getUserEmail().map {
                _accountEmail.postValue(it)
            }.launchIn(viewModelScope)
            preferenceStorage.getUserFirstName().map {
                _accountFirstName.postValue(it)
            }.launchIn(viewModelScope)
            preferenceStorage.getUserLastName().map {
                _accountLastName.postValue(it)
            }.launchIn(viewModelScope)
            preferenceStorage.getBusinessName().map {
                _businessName.postValue(it)
            }.launchIn(viewModelScope)
            preferenceStorage.getBusinessAddress().map {
                _businessAddress.postValue(it)
            }.launchIn(viewModelScope)
            preferenceStorage.getBusinessPhone().map {
                _businessPhone.postValue(it)
            }.launchIn(viewModelScope)
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
                        Timber.d("saving user data in preference")
                        preferenceStorage.saveUserFirstName(result.data.firstName)
                        preferenceStorage.saveUserLastName(result.data.lastName)
                        preferenceStorage.saveBusinessName(result.data.businessName)
                        preferenceStorage.saveBusinessAddress(result.data.businessAddress)
                        preferenceStorage.saveBusinessPhone(result.data.businessPhone)
                    }
                    else -> {}
                }
            }

    fun changePassword(password: OldNewPassword): Flow<Result<StringResponse>> =
        changePasswordUseCase(password)

    fun signOut(): Flow<Boolean> = flow {
        preferenceStorage.clearDatastore()
        deleteAllDatabaseDataUseCase()
            .collect { result ->
                when (result) {
                    is Result.Success -> emit(true)
                    else -> emit(false)
                }
            }
    }
}