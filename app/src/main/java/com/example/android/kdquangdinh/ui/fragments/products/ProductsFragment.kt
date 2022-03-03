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
                    Log.d("HAHA", "inside remove a")
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
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_products, container, false)
        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        binding.registerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_registerFragment)
        }

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_productsFragment_to_loginFragment)
        }

        mainViewModel.readToken.asLiveData().observe(viewLifecycleOwner, { value ->
            mainViewModel.token = value
            if (!value.isNullOrEmpty()) {
                binding.addProductBtn.visibility = View.VISIBLE
            }
            mainViewModel.getAllProducts(value)
        })

        setupRecyclerView()

        mainViewModel.getAllProductsResult.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    mAdapter.addHeaderAndSubmitList(
                        response.data,
                        mainViewModel.token.isNullOrEmpty()
                    )
//                        findNavController().navigate(R.id.action_registerFragment_to_productsFragment)
                }
                is NetworkResult.Error -> {
//                    Toast.makeText(
//                        requireContext(),
//                        response.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
                is NetworkResult.Loading -> {
//
                }
            }
        })

        mainViewModel.addProductResult.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    mAdapter.addProduct(response.data!!)
//                        findNavController().navigate(R.id.action_registerFragment_to_productsFragment)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
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
//                        findNavController().navigate(R.id.action_registerFragment_to_productsFragment)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
//
                }
            }
        })

        mainViewModel.removeProductResult.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Log.d("HAHA", "before update adapter")
                    mAdapter.removeProduct(response.data!!)
//                        findNavController().navigate(R.id.action_registerFragment_to_productsFragment)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
//
                }
            }
        })

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

        mainViewModel.searchProductResult.observe(viewLifecycleOwner, { response ->
            if (response.data == null) {

            } else {
                val list = mutableListOf<Product>(response.data!!)

                when (response) {
                    is NetworkResult.Success -> {
                        mAdapter.addHeaderAndSubmitList(list, mainViewModel.token.isNullOrEmpty())
//                        findNavController().navigate(R.id.action_registerFragment_to_productsFragment)
                    }
                    is NetworkResult.Error -> {
//                    Toast.makeText(
//                        requireContext(),
//                        response.message,
//                        Toast.LENGTH_SHORT
//                    ).show()
                    }
                    is NetworkResult.Loading -> {
//
                    }
                }
            }


        })

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }
}