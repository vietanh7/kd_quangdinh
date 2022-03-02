package com.example.android.kdquangdinh.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.kdquangdinh.data.database.entities.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(RecipesTypeConverter::class)
abstract class ProductsDatabase: RoomDatabase() {

    abstract fun productsDao(): ProductsDAO

}