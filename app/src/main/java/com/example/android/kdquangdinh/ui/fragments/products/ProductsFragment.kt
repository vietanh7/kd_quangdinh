package com.example.android.kdquangdinh.ui.fragments.products

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.kdquangdinh.R
import com.example.android.kdquangdinh.adapters.ProductListener
import com.example.android.kdquangdinh.adapters.ProductsAdapter
import com.example.android.kdquangdinh.databinding.FragmentProductsBinding
import com.example.android.kdquangdinh.models.Product
import com.example.android.kdquangdinh.util.AddProductDialogFragment
import com.example.android.kdquangdinh.util.NetworkResult
import com.example.android.kdquangdinh.util.UpdateProductDialogFragment
import com.example.android.kdquangdinh.viewmodels.MainViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [ProductsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy {
        ProductsAdapter(
            ProductListener({
                mainViewModel.updatedProduct = it
                UpdateProductDialogFragment().show(
                    childFragmentManager, AddProductDialogFragment.TAG
                )
            },
                {

                    mainViewModel.removeProduct(mainViewModel.token, it)
                })
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        setUpObservers()
        setUpClickListeners()
        setupRecyclerView()

        return binding.root
    }

    private fun setUpObservers() {
        mainViewModel.readToken.asLiveData().observe(viewLifecycleOwner, { value ->
            mainViewModel.token = value
            if (!value.isNullOrEmpty()) {
                binding.addProductBtn.visibility = View.VISIBLE
                binding.searchBtn.visibility = View.VISIBLE
                binding.seachEt.visibility = View.VISIBLE
            }
            mainViewModel.getAllProducts(value)
        })

        mainViewModel.getAllProductsResult.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    mAdapter.addHeaderAndSubmitList(
                        response.data,
                        mainViewModel.token.isNullOrEmpty()
                    )
                }
                is NetworkResult.Error -> {
                    showToast(response.message)
                }
                is NetworkResult.Loading -> {

                }
            }
        })

        mainViewModel.addProductResult.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    mAdapter.addProduct(response.data!!)
                    showToast(getString(R.string.add_product_success))
                }
                is NetworkResult.Error -> {
                    showToast(response.message)
                }
                is NetworkResult.Loading -> {
//
                }
            }
        })


        mainViewModel.updateProductResult.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    if (response.data != null)
                        mAdapter.updateProduct(response.data)
                    showToast(getString(R.string.update_product_success))
                }
                is NetworkResult.Error -> {
                    showToast(response.message)
                }
                is NetworkResult.Loading -> {
                }
            }
        })

        mainViewModel.removeProductResult.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {

                    mAdapter.removeProduct(response.data!!)
                    showToast(getString(R.string.remove_product_success))
                }
                is NetworkResult.Error -> {
                    showToast(response?.message)
                }
                is NetworkResult.Loading -> {

                }
            }
        })

        mainViewModel.searchProductResult.observe(viewLifecycleOwner, { response ->
            if (response.data == null) {
                binding.recyclerview.visibility = View.GONE
            } else {
                val list = mutableListOf<Product>(response.data!!)
                when (response) {
                    is NetworkResult.Success -> {
                        mAdapter.addHeaderAndSubmitList(list, mainViewModel.token.isNullOrEmpty())
                    }
                    is NetworkResult.Error -> {
                        showToast(response?.message)
                    }
                    is NetworkResult.Loading -> {
                    }
                }
            }
        })
    }

    private fun setUpClickListeners() {
        binding.registerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_registerFragment)
        }

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_loginFragment)
        }

        binding.addProductBtn.setOnClickListener({
            AddProductDialogFragment().show(
                childFragmentManager, AddProductDialogFragment.TAG
            )
        })

        binding.searchBtn.setOnClickListener({
            val searchQuery = binding.seachEt.text.toString()
            if (searchQuery.isNullOrEmpty()) {
                mainViewModel.getAllProducts(mainViewModel.token)
            } else {
                mainViewModel.searchProducts(mainViewModel.token, searchQuery)
            }
        })
    }

    private fun showToast(message: String?) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }
}