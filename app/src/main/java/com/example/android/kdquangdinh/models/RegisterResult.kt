package com.example.android.kdquangdinh.models

import com.google.gson.annotations.SerializedName


data class RegisterResult (
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("email" ) var email : ArrayList<String>? = arrayListOf(),
    @SerializedName("password") var password : ArrayList<String>? = arrayListOf()
)