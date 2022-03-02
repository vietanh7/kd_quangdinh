package com.example.android.kdquangdinh.models

import com.google.gson.annotations.SerializedName

data class LoginResult (
    @SerializedName("token" ) var token : String? = null,
    @SerializedName("error" ) var error : String?  = null,
)