package com.example.android.kdquangdinh.data.network

import com.example.android.kdquangdinh.models.GetAllProductsResult
import com.example.android.kdquangdinh.models.LoginResult
import com.example.android.kdquangdinh.models.Product
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

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResult>


    @GET("items")
    suspend fun getAllProducts(
        @Header("Authorization") authHeader: String?
    ): Response<GetAllProductsResult>

    @FormUrlEncoded
    @POST("item/add")
    suspend fun addProduct(
        @Header("Authorization") authHeader: String?,
        @Field("sku") sku: String,
        @Field("product_name") product_name: String,
        @Field("qty") qty: Int,
        @Field("price") price: Float,
        @Field("unit") unit: String,
        @Field("status") status: Int,
    ): Response<Product>

    @FormUrlEncoded
    @POST("item/delete")
    suspend fun removeProduct(
        @Header("Authorization") authHeader: String?,
        @Field("sku") sku: String,
    ): Response<Product>

    @FormUrlEncoded
    @POST("item/search")
    suspend fun searchProducts(
        @Header("Authorization") authHeader: String?,
        @Field("sku") sku: String,
    ): Response<Product>



}