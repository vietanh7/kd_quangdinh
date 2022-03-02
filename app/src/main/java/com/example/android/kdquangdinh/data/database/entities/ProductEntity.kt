package com.example.android.kdquangdinh.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.kdquangdinh.util.Constants.Companion.PRODUCTS_TABLE

@Entity(tableName = PRODUCTS_TABLE)
data class ProductEntity(
    @PrimaryKey
    val id: Long,
    val name: String?,
    val quantity: Int?,
    val price: Float?,
    val unit: String?,
)

