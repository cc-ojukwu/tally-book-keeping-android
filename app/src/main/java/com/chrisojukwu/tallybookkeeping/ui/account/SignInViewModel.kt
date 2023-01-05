package com.chrisojukwu.tallybookkeeping.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chrisojukwu.tallybookkeeping.domain.model.SignInUser
import com.chrisojukwu.tallybookkeeping.domain.model.User
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import com.chrisojukwu.tallybookkeeping.domain.model.Token
import com.chrisojukwu.tallybookkeeping.domain.model.TokenWithEmail
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.chrisojukwu.tallybookkeeping.domain.usecase.CreateNewAccountUseCase
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
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase
) : ViewModel() {

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showTextView = MutableLiveData<Boolean>()
    val showTextView = _showTextView.value

    private val _jwtToken = MutableLiveData<String>()
    val jwtToken: LiveData<String> = _jwtToken

    fun createAccount(user: User): Flow<Result<String>> =
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

}


//    fun createAccount(email: String, password: String): LiveData<ServerResponse> {
//        val result = MutableLiveData<ServerResponse>()
//        _isLoading.value = true
//        viewModelScope.launch {
//            val user = User(email, password, getRandomUserId(), Provider.LOCAL)
//            try {
//                val response = apiService.createNewUserAccount(user)
//                if (response.isSuccessful) {
//                    _isLoading.postValue(false)
//                    result.postValue(ServerResponse.SUCCESS)
//                    Timber.d("server success")
//                } else if (response.code() == 400) {
//                    _isLoading.postValue(false)
//                    result.postValue(ServerResponse.DUPLICATE)
//                } else {
//                    _isLoading.postValue(false)
//                    result.postValue(ServerResponse.FAILURE)
//                }
//            } catch (e: Exception) {
//                println(e.localizedMessage)
//                _isLoading.postValue(false)
//                Timber.d("exception in api call-create account")
//                result.postValue(ServerResponse.FAILURE)
//            }
//        }
//        return result
//    }