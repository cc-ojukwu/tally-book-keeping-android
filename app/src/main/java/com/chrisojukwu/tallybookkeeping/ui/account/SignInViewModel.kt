package com.chrisojukwu.tallybookkeeping.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisojukwu.tallybookkeeping.domain.model.Provider
import com.chrisojukwu.tallybookkeeping.domain.model.SignInUser
import com.chrisojukwu.tallybookkeeping.domain.model.User
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import com.chrisojukwu.tallybookkeeping.data.source.remote.ApiService
import com.chrisojukwu.tallybookkeeping.utils.getRandomUserId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    private val apiService: ApiService
) : ViewModel() {

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showTextView = MutableLiveData<Boolean>()
    val showTextView = _showTextView.value

    private val _jwtToken = MutableLiveData<String>()
    val jwtToken: LiveData<String> = _jwtToken

    fun createAccount(email: String, password: String): LiveData<ServerResponse> {
        val result = MutableLiveData<ServerResponse>()
        _isLoading.value = true
        viewModelScope.launch {
            val user = User(email, password, getRandomUserId(), Provider.LOCAL)
            try {
                val response = apiService.createNewUserAccount(user)
                if (response.isSuccessful) {
                    _isLoading.postValue(false)
                    result.postValue(ServerResponse.SUCCESS)
                    Timber.d("server success")
                } else if (response.code() == 400) {
                    _isLoading.postValue(false)
                    result.postValue(ServerResponse.DUPLICATE)
                } else {
                    _isLoading.postValue(false)
                    result.postValue(ServerResponse.FAILURE)
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
                _isLoading.postValue(false)
                Timber.d("exception in api call-create account")
                result.postValue(ServerResponse.FAILURE)
            }
        }
        return result
    }


    fun login(email: String, password: String): LiveData<ServerResponse> {
        val result = MutableLiveData<ServerResponse>()
        _isLoading.value = true
        viewModelScope.launch {
            val user = SignInUser(email, password)
            try {
                val response = apiService.signInUser(user)
                if (response.isSuccessful) {
                    val jwt = response.body()!!.jwtToken
                    if (jwt.isNotEmpty()) {
                        _isLoading.postValue(false)
                        result.postValue(ServerResponse.SUCCESS)
                        preferenceStorage.authenticateUserToken(jwt)
                        Timber.d("couldn't reach server")
                    } else {
                        _isLoading.postValue(false)
                        result.postValue(ServerResponse.FAILURE)
                        Timber.d("jwt is empty")
                    }
                } else {
                    _isLoading.postValue(false)
                    result.postValue(ServerResponse.FAILURE)
                }
            } catch (e: Exception) {
                _isLoading.postValue(false)
                Timber.d("exception in api call-sign in")
                result.postValue(ServerResponse.UNAVAILABLE)
            }
        }

        return result
    }

    fun googleLogin(idToken: String): LiveData<ServerResponse> {
        val result = MutableLiveData<ServerResponse>()
        viewModelScope.launch {
            try {
                val response = apiService.googleSignIn(idToken)
                if (response.isSuccessful) {
                    Timber.d("server success")
                    val token = response.body()?.jwtToken
                    if (token != null) {
                        _jwtToken.postValue(token!!)
                        result.postValue(ServerResponse.SUCCESS)
                        preferenceStorage.authenticateUserToken(token)
                        preferenceStorage.userSignInStatus(true)
                    } else {
                        result.postValue(ServerResponse.FAILURE)
                        Timber.e("google login token is null")
                    }
                } else {
                    result.postValue(ServerResponse.UNAVAILABLE)
                    Timber.e("google login token - server-side error")

                }

            } catch (e: Exception) {
                result.postValue(ServerResponse.FAILURE)
                Timber.e("google login token error - failed")

            }
        }
        return result
    }

}

enum class ServerResponse {
    SUCCESS, FAILURE, DUPLICATE, UNAVAILABLE
}