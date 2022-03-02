package com.example.android.kdquangdinh.adapters

class ProductsAdapter {
}

sealed class DataItem {
    data class ProductItem(val sleepNight: SleepNight): DataItem() {
        override val id = sleepNight.nightId
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}