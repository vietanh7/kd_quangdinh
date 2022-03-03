package com.example.android.kdquangdinh.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.kdquangdinh.R
import com.example.android.kdquangdinh.databinding.ProductRowLayoutBinding
import com.example.android.kdquangdinh.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class ProductsAdapter(val clickListener: ProductListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(ProductDiffCallback()){

    var currentItems : MutableList<DataItem> = mutableListOf()
    var areButtonsDisabled = true

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: MutableList<Product>?, areButtonsDisabled: Boolean) {
        this.areButtonsDisabled = areButtonsDisabled

        adapterScope.launch {
            currentItems = when (list) {
                null -> mutableListOf(DataItem.Header)
                else -> (mutableListOf(DataItem.Header) + list.map { DataItem.ProductItem(it, areButtonsDisabled) }) as MutableList<DataItem>
            }
            withContext(Dispatchers.Main) {
                submitList(currentItems)
            }
        }
    }

    fun addProduct(product: Product) {

        adapterScope.launch {
            currentItems =
                (currentItems + mutableListOf(DataItem.ProductItem(product, areButtonsDisabled))) as MutableList<DataItem>
            withContext(Dispatchers.Main) {
                submitList(currentItems)
            }
        }
    }


    fun updateProduct(product: Product) {

        adapterScope.launch {
//        for (item in currentItems) {
//            if (item.sku.equals(product.sku)) {
//
//            }
//
//        }
            Log.d("HAHA", "before edit size: " + currentItems.size)
            currentItems.forEachIndexed { index, element ->
                if (element.sku.equals(product.sku)) {
                    currentItems[index] = (DataItem.ProductItem(product, areButtonsDisabled))
                }
            }
//            currentItems.filterNot { it ->
//
//                if (it.sku.equals(product.sku)) {
//                    Log.d("HAHA", "find matching ")
//                }
//                it.sku.equals(product.sku) }
//            Log.d("HAHA", "after edit size: " + currentItems.size)
//            currentItems = currentItems + listOf(DataItem.ProductItem(product, areButtonsDisabled))
            withContext(Dispatchers.Main) {
                submitList(currentItems)
                notifyDataSetChanged()
            }
        }
    }


    fun removeProduct(product: Product) {

        currentItems =
            (currentItems - mutableListOf(DataItem.ProductItem(product, areButtonsDisabled))) as MutableList<DataItem>

        adapterScope.launch {
            withContext(Dispatchers.Main) {
                submitList(currentItems)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ProductItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class HeaderHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): HeaderHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header_row_layout, parent, false)
                return HeaderHolder(view)
            }
        }
    }

    class ViewHolder private constructor(val binding: ProductRowLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item: Product, areButtonsDisabled: Boolean, clickListener: ProductListener) {
            binding.product = item
            binding.areButtonsDisabled = areButtonsDisabled
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProductRowLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val productItem = getItem(position) as DataItem.ProductItem
                holder.bind(productItem.product, productItem.areButtonsDisabled, clickListener)
            }
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.sku == newItem.sku
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

open class ProductListener(public val updateListener: (product: Product) -> Unit, public val removeListener: (productSku: String) -> Unit) {
    fun onUpdateClick(product: Product) = updateListener(product)
    fun onRemoveClick(product: Product) = removeListener(product.sku)
}

sealed class DataItem {
    data class ProductItem(val product: Product, val areButtonsDisabled: Boolean ): DataItem() {
        override val sku = product.sku
    }

    object Header: DataItem() {
        override val sku = "header"
    }

    abstract val sku: String
}