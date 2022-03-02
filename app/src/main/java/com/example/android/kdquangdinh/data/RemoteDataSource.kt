package com.example.android.kdquangdinh.data

import android.util.Log
import com.example.android.kdquangdinh.data.network.ProductsApi
import com.example.android.kdquangdinh.models.LoginResult
import com.example.android.kdquangdinh.models.RegisterResult
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val productsApi: ProductsApi
) {

    suspend fun register(email: String, password: String): Response<RegisterResult> {
        return productsApi.register(email, password)
    }

    suspend fun login(email: String, password: String): Response<LoginResult> {
        return productsApi.login(email, password)
    }
}