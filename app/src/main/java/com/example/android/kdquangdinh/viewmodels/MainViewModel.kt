package com.example.android.kdquangdinh.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.kdquangdinh.R
import com.example.android.kdquangdinh.data.DataStoreRepository
import com.example.android.kdquangdinh.data.Repository
import com.example.android.kdquangdinh.models.GetAllProductsResult
import com.example.android.kdquangdinh.models.LoginResult
import com.example.android.kdquangdinh.models.Product
import com.example.android.kdquangdinh.models.RegisterResult
import com.example.android.kdquangdinh.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    var token: String? = ""
    var updatedProduct: Product? = null
    var addedProduct: MutableLiveData<Product> = MutableLiveData()

    /** RETROFIT */
    var registerResult: MutableLiveData<NetworkResult<RegisterResult>> = MutableLiveData()
    var loginResult: MutableLiveData<NetworkResult<LoginResult>> = MutableLiveData()
    var getAllProductsResult: MutableLiveData<NetworkResult<GetAllProductsResult>> = MutableLiveData()
    var addProductResult: MutableLiveData<NetworkResult<Product>> = MutableLiveData()
    var removeProductResult: MutableLiveData<NetworkResult<Product>> = MutableLiveData()
    var searchProductResult: MutableLiveData<NetworkResult<Product>> = MutableLiveData()
    var updateProductResult: MutableLiveData<NetworkResult<Product>> = MutableLiveData()

    val readToken = dataStoreRepository.readToken

    fun register(email: String, password: String) = viewModelScope.launch {
        registerSafeCall(email, password)
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        loginSafeCall(email, password)
    }

    fun getAllProducts(token: String?) = viewModelScope.launch {
        getAllProductsSafeCall(token)
    }

    fun searchProducts(token: String?, searchQuery: String) = viewModelScope.launch {
        searchProductsSafeCall(token, searchQuery)
    }

    fun updateProduct(token: String?, product: Product) = viewModelScope.launch {
        updateProductSafeCall(token, product)
    }

    private suspend fun updateProductSafeCall(token: String?, product: Product) {
        updateProductResult.value = NetworkResult.Loading()

        try {

            val response = repository.remote.updateProduct(token, product)
            Log.d("HAHA", response.toString())
            updateProductResult.value = handleUpdateProductResponse(response)


        } catch (e: Exception) {
            updateProductResult.value =
                NetworkResult.Error(getApplication<Application>().resources.getString(R.string.update_product_failed_error))
        }
    }

    private fun handleUpdateProductResponse(response: Response<Product>): NetworkResult<Product>? {
        when {
            response.isSuccessful -> {
                val result = response.body()
                if (result != null) {
                    return NetworkResult.Success(result)
                } else {
                    return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.update_product_failed_error))
                }
            }
            else -> {
                //error message in response is empty so we show default error message
                return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.update_product_failed_error))
            }
        }
    }

    private suspend fun searchProductsSafeCall(token: String?, searchQuery: String) {
        Log.d("HAHA", "inside searchProductsSafeCall")
        searchProductResult.value = NetworkResult.Loading()

        try {

            val response = repository.remote.searchProducts(token, searchQuery)
            searchProductResult.value = handleSearchProductsResponse(response)


        } catch (e: Exception) {
            Log.d("HAHA", "inside exception " + e.stackTrace)
            searchProductResult.value =
                NetworkResult.Error(getApplication<Application>().resources.getString(R.string.get_all_products_failed_error))
        }
    }

    private fun handleSearchProductsResponse(response: Response<Product>): NetworkResult<Product>? {
        Log.d("HAHA", "search response is: " + response.toString())
        when {

            response.isSuccessful -> {
                val result = response.body()
                if (result != null) {
                    return NetworkResult.Success(result)
                } else {
                    return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.search_product_failed_error))
                }
            }
            else -> {
                //error message in response is empty so we show default error message
                return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.search_product_failed_error))
            }
        }
    }

    fun addProduct(token: String?, product: Product) = viewModelScope.launch {
        addProductSafeCall(token, product)
    }

    fun removeProduct(token: String?, productSku: String) = viewModelScope.launch {
        removeProductSafeCall(token, productSku)
    }

    private suspend fun removeProductSafeCall(token: String?, productSku: String) {
        removeProductResult.value = NetworkResult.Loading()

        try {

            val response = repository.remote.removeProduct(token, productSku)
            Log.d("HAHA", response.toString())
            removeProductResult.value = handleRemoveProductResponse(response)


        } catch (e: Exception) {
            removeProductResult.value =
                NetworkResult.Error(getApplication<Application>().resources.getString(R.string.remove_product_failed_error))
        }
    }

    private fun handleRemoveProductResponse(response: Response<Product>): NetworkResult<Product>? {
        when {

            response.isSuccessful -> {
                val result = response.body()
                if (result != null) {
                    return NetworkResult.Success(result)
                } else {
                    return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.remove_product_failed_error))
                }
            }
            else -> {
                //error message in response is empty so we show default error message
                return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.remove_product_failed_error))
            }
        }
    }

    private suspend fun addProductSafeCall(token: String?, product: Product) {
        addProductResult.value = NetworkResult.Loading()

        try {

            val response = repository.remote.addProduct(token, product)
            Log.d("HAHA", response.toString())
            addProductResult.value = handleAddProductResponse(response)


        } catch (e: Exception) {
            addProductResult.value =
                NetworkResult.Error(getApplication<Application>().resources.getString(R.string.add_product_failed_error))
        }
    }

    private fun handleAddProductResponse(response: Response<Product>): NetworkResult<Product>? {
        when {

            response.isSuccessful -> {
                val result = response.body()
                if (result != null) {
                    return NetworkResult.Success(result)
                } else {
                    return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.add_product_failed_error))
                }
            }
            else -> {
                //error message in response is empty so we show default error message
                return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.add_product_failed_error))
            }
        }
    }

    private suspend fun getAllProductsSafeCall(token: String?) {
        getAllProductsResult.value = NetworkResult.Loading()

        try {

            val response = repository.remote.getAllProducts(token)
            getAllProductsResult.value = handleGetAllProductsResponse(response)


        } catch (e: Exception) {
            getAllProductsResult.value =
                NetworkResult.Error(getApplication<Application>().resources.getString(R.string.get_all_products_failed_error))
        }
    }

    private fun handleGetAllProductsResponse(response: Response<GetAllProductsResult>): NetworkResult<GetAllProductsResult>? {
        when {

            response.isSuccessful -> {
                val result = response.body()
                if (!result.isNullOrEmpty()) {
                    return NetworkResult.Success(result)
                } else {
                    return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.get_all_products_failed_error))
                }
            }
            else -> {
                //error message in response is empty so we show default error message
                return NetworkResult.Error(getApplication<Application>().resources.getString(R.string.get_all_products_failed_error))
            }
        }
    }

    private fun saveToken(token: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveToken(token)
        }

    private suspend fun loginSafeCall(email: String, password: String) {
        Log.d("HAHA", "LOGIN")
        loginResult.value = NetworkResult.Loading()

        try {

            val response = repository.remote.login(email, password)
            loginResult.value = handleLoginResponse(response)


        } catch (e: Exception) {
            loginResult.value =
                NetworkResult.Error(getApplication<Application>().resources.getString(R.string.login_failed_error))
        }
    }

    private fun handleLoginResponse(response: Response<LoginResult>): NetworkResult<LoginResult>? {
        Log.d("HAHA", response.toString())
        when {

            response.isSuccessful -> {
                val loginResult = response.body()
                if (loginResult?.token != null) {
                    saveToken(loginResult?.token)
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
        registerResult.value = NetworkResult.Loading()

        try {

            val response = repository.remote.register(email, password)
            registerResult.value = handleRegisterResponse(response)


        } catch (e: Exception) {
            registerResult.value =
                NetworkResult.Error(getApplication<Application>().resources.getString(R.string.register_failed_error))
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