package com.example.android.kdquangdinh.models

import com.google.gson.annotations.SerializedName

data class Product (

    @SerializedName("sku"          ) var sku         : String? = null,
    @SerializedName("product_name" ) var productName : String? = null,
    @SerializedName("qty"          ) var qty         : String? = null,
    @SerializedName("price"        ) var price       : String? = null,
    @SerializedName("unit"         ) var unit        : String? = null,
    @SerializedName("status"       ) var status      : String? = null,
    @SerializedName("updated_at"   ) var updatedAt   : String? = null,
    @SerializedName("created_at"   ) var createdAt   : String? = null,
    @SerializedName("id"           ) var id          : Long?    = null

)