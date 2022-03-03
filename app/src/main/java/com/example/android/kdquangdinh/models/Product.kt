package com.example.android.kdquangdinh.models

import com.google.gson.annotations.SerializedName

data class Product (

    @SerializedName("sku"          ) var sku         : String = "",
    @SerializedName("product_name" ) var productName : String = "",
    @SerializedName("qty"          ) var qty         : Int = 0,
    @SerializedName("price"        ) var price       : Float = 0F,
    @SerializedName("unit"         ) var unit        : String = "",
    @SerializedName("status"       ) var status      : Int = Int.MIN_VALUE,
    @SerializedName("updated_at"   ) var updatedAt   : String? = null,
    @SerializedName("created_at"   ) var createdAt   : String? = null,
    @SerializedName("id"           ) var id          : Long    = Long.MIN_VALUE

)