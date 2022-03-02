package com.example.android.kdquangdinh.data

import com.example.android.kdquangdinh.data.network.ProductsApi
import com.example.android.kdquangdinh.models.RegisterResult
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val productsApi: ProductsApi
) {

    suspend fun register(email: String, password: String): Response<RegisterResult> {
        return productsApi.register(email, password)
    }
}