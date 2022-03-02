package com.example.android.kdquangdinh.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.kdquangdinh.R
import com.example.android.kdquangdinh.data.Repository
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

    fun register(email: String, password: String) = viewModelScope.launch {
        registerSafeCall(email, password)
    }

    private suspend fun registerSafeCall(email: String, password: String) {
        registerResult.value =NetworkResult.Loading()

        try {

            val response = repository.remote.register(email, password)
            registerResult.value = handleRegisterResponse(response)


        } catch (e: Exception) {
            registerResult.value = NetworkResult.Error(getApplication<Application>().resources.getString(R.string.register_faile_error))
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
                    return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.register_faile_error))
                }

            }
            else -> {
                return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.register_faile_error))
            }
        }
    }
}