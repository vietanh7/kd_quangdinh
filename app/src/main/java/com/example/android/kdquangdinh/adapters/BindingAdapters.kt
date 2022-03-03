package com.example.android.kdquangdinh.adapters

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter

class BindingAdapters {

    companion object {

        @BindingAdapter("setPrice", requireAll = false)
        @JvmStatic
        fun setPrice(view: View, price: Float) {
            if(view is EditText){
//                    view.text = price.toString()
            }
        }

    }

}