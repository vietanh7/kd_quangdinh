package com.example.android.kdquangdinh.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.kdquangdinh.R
import com.example.android.kdquangdinh.databinding.AddProductLayoutBinding
import com.example.android.kdquangdinh.databinding.FragmentProductsBinding
import com.example.android.kdquangdinh.models.Product
import com.example.android.kdquangdinh.viewmodels.MainViewModel
import java.lang.Exception

class AddProductDialogFragment : DialogFragment() {

    private var _binding: AddProductLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        _binding = AddProductLayoutBinding.inflate(LayoutInflater.from(context))

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setView(binding.root)

                .setPositiveButton(R.string.ok,
                    DialogInterface.OnClickListener { dialog, id ->
                        try {
                            mainViewModel.addProduct(
                                mainViewModel.token, Product(
                                    sku = binding.skuEdt.text.toString(),
                                    productName = binding.nameEt.text.toString(),
                                    qty = binding.quantityEt.text.toString().toInt(),
                                    price = binding.priceEt.text.toString().toFloat(),
                                    unit = binding.unitEt.text.toString(),
                                    status = binding.statusEt.text.toString().toInt()
                                )
                            )
                        } catch (e: Exception) {
                            mainViewModel.error.value = getString(R.string.product_field_not_valid)
                        }
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
                .setTitle(R.string.add_product)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        const val TAG = "AddProductDialogFragment"
    }
}