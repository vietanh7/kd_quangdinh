package com.example.android.kdquangdinh.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.kdquangdinh.R
import com.example.android.kdquangdinh.data.Repository
import com.example.android.kdquangdinh.models.LoginResult
import com.example.android.kdquangdinh.models.RegisterResult
import com.example.android.kdquangdinh.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {


    /** RETROFIT */
    var registerResult: MutableLiveData<NetworkResult<RegisterResult>> = MutableLiveData()
    var loginResult: MutableLiveData<NetworkResult<LoginResult>> = MutableLiveData()

    fun register(email: String, password: String) = viewModelScope.launch {
        registerSafeCall(email, password)
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        loginSafeCall(email, password)
    }

    private suspend fun loginSafeCall(email: String, password: String) {
        Log.d("HAHA", "LOGIN")
        loginResult.value =NetworkResult.Loading()

        try {

            val response = repository.remote.login(email, password)
            loginResult.value = handleLoginResponse(response)


        } catch (e: Exception) {
            loginResult.value = NetworkResult.Error(getApplication<Application>().resources.getString(R.string.login_failed_error))
        }
    }

    private fun handleLoginResponse(response: Response<LoginResult>): NetworkResult<LoginResult>? {
        Log.d("HAHA", response.toString())
        when {

            response.isSuccessful -> {
                val loginResult = response.body()
                if (loginResult?.token != null) {
                    return NetworkResult.Success(loginResult)
                } else {
                    return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.login_failed_error))
                }

            }
            else -> {
                //error message in response is empty so we show default error message
                return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.login_failed_error))
            }
        }
    }

    private suspend fun registerSafeCall(email: String, password: String) {
        Log.d("HAHA", "REGISTER")
        registerResult.value =NetworkResult.Loading()

        try {

            val response = repository.remote.register(email, password)
            registerResult.value = handleRegisterResponse(response)


        } catch (e: Exception) {
            registerResult.value = NetworkResult.Error(getApplication<Application>().resources.getString(R.string.register_failed_error))
        }

    }

    private fun handleRegisterResponse(response: Response<RegisterResult>): NetworkResult<RegisterResult>? {
        Log.d("HAHA", response.toString())
        when {

            response.isSuccessful -> {
                val registerResult = response.body()
                if (registerResult?.success == true) {
                    return NetworkResult.Success(registerResult)
                } else {
                    return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.register_failed_error))
                }

            }
            else -> {
                //error message in response is empty so we show default error message
                return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.register_failed_error))
            }
        }
    }
}