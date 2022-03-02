package com.example.android.kdquangdinh.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.android.kdquangdinh.data.database.entities.ProductEntity

@Dao
interface ProductsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(productsEntity: List<ProductEntity>)
}