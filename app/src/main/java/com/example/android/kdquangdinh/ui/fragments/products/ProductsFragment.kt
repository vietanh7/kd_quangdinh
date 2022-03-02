package com.example.android.kdquangdinh.ui.fragments.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.android.kdquangdinh.R
import com.example.android.kdquangdinh.databinding.FragmentProductsBinding
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

        })

        return binding.root
    }


}