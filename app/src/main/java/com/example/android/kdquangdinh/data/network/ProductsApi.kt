package com.example.android.kdquangdinh.data.network

import com.example.android.kdquangdinh.models.RegisterResult
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ProductsApi {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<RegisterResult>

}