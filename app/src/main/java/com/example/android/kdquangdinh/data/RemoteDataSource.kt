package com.example.android.kdquangdinh.data

import android.util.Log
import com.example.android.kdquangdinh.data.network.ProductsApi
import com.example.android.kdquangdinh.models.GetAllProductsResult
import com.example.android.kdquangdinh.models.LoginResult
import com.example.android.kdquangdinh.models.Product
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

    suspend fun getAllProducts(token: String?): Response<GetAllProductsResult> {
        return productsApi.getAllProducts("Bearer " + token)
    }

    suspend fun addProduct(token: String?, product: Product): Response<Product> {
        Log.d("HAHA", "token is: " + token)
        return productsApi.addProduct(
            "Bearer " + token,
            product_name = product.productName,
            sku = product.sku,
            qty = product.qty,
            price = product.price,
            unit = product.unit,
            status = product.status
        )

    }
}