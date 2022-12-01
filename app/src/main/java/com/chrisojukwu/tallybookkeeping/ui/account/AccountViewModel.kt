package com.chrisojukwu.tallybookkeeping.ui.account

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisojukwu.tallybookkeeping.data.models.Provider
import com.chrisojukwu.tallybookkeeping.data.models.SignInUser
import com.chrisojukwu.tallybookkeeping.data.models.User
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import com.chrisojukwu.tallybookkeeping.data.source.remote.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AccountViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : ViewModel() {

    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showTextView = MutableLiveData<Boolean>()
    val showTextView = _showTextView.value

    private val _jwtToken = MutableLiveData<String>()
    val jwtToken: LiveData<String> = _jwtToken

//    @BindingAdapter("visibleGone")
//    fun showHide(view: View, show: Boolean) {
//        view.visibility = if (show) View.VISIBLE else View.GONE
//    }

    fun createAccount(email: String, password: String): LiveData<ServerResponse> {
        val result = MutableLiveData<ServerResponse>()
        _isLoading.value = true
        viewModelScope.launch {
            val userId =
                "user${(0..10).random()}${(0..20).random()}${(0..30).random()}${(0..50).random()}${(0..99).random()}"
            val user = User(email, password, userId, Provider.LOCAL)
            try {
                val response = Api.retrofitService.createNewUserAccount(user)
                //Log.e("response.code()-", "${response.code()}")
                if (response.isSuccessful) {
                    _isLoading.postValue(false)
                    result.postValue(ServerResponse.SUCCESS)
                    Log.d("good-signup", "server success")
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
                Log.d("error-signup", "couldn't reach server")
            }
        }
        return result
    }


    fun login(email: String, password: String): LiveData<ServerResponse> {
        val result = MutableLiveData<ServerResponse>()
        _isLoading.value = true
        viewModelScope.launch {
            //delay(5000L)
            val user = SignInUser(email, password)
            try {
                val response = Api.retrofitService.signInUser(user)
                if (response.isSuccessful) {
                    val jwt = response.body()!!.jwtToken
                    if (!jwt.isEmpty()) {
                        _isLoading.postValue(false)
                        result.postValue(ServerResponse.SUCCESS)
                        preferenceStorage.authenticateUserToken(jwt)
                        //preferenceStorage.userSignInStatus(true)
                        Log.d("good-login", "server success")
                    } else {
                        _isLoading.postValue(false)
                        result.postValue(ServerResponse.FAILURE)
                        Log.d("error-login", "jwt is empty")
                    }
                } else {
                    _isLoading.postValue(false)
                    result.postValue(ServerResponse.FAILURE)
                }
            } catch (e: Exception) {
                _isLoading.postValue(false)
                Timber.d("exception in api call-sign in")
                result.postValue(ServerResponse.UNAVAILABLE)
                Log.d("error-login", "couldn't reach server")

            }
        }

        return result
    }

    fun googleLogin(idToken: String): LiveData<ServerResponse> {
        val result = MutableLiveData<ServerResponse>()
        viewModelScope.launch {
            try {
                val response = Api.retrofitService.googleSignIn(idToken)
                if (response.isSuccessful) {
                    Log.d("good-google", "server success")
                    val token = response.body()?.jwtToken
                    if (token != null) {
                        _jwtToken.postValue(token!!)
                        result.postValue(ServerResponse.SUCCESS)
                        preferenceStorage.authenticateUserToken(token)
                        preferenceStorage.userSignInStatus(true)
                    } else {
                        result.postValue(ServerResponse.FAILURE)
                        Timber.e("google login token is null")
                        Log.d("failure", "no token for server")
                    }
                } else {
                    result.postValue(ServerResponse.UNAVAILABLE)
                    Timber.e("google login token - server-side error")
                    Log.d("failure", "server-side error")

                }

            } catch (e: Exception) {
                result.postValue(ServerResponse.FAILURE)
                Timber.e("google login token error - failed")
                Log.d("failure", "my server failed")

            }
        }
        return result
    }

}

enum class ServerResponse {
    SUCCESS, FAILURE, DUPLICATE, UNAVAILABLE
}