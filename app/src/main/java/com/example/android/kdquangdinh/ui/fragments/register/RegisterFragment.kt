package com.example.android.kdquangdinh.ui.fragments.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.kdquangdinh.R
import com.example.android.kdquangdinh.databinding.FragmentRegisterBinding
import com.example.android.kdquangdinh.util.NetworkResult
import com.example.android.kdquangdinh.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
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

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)


        binding.registerButton.setOnClickListener {
            val email = binding.emailEdittext.text.toString().trim()
            val password = binding.passwordEdittext.text.toString().trim()
            //TO-DO validate email and password
            mainViewModel.register(email, password)
            mainViewModel.registerResult.observe(viewLifecycleOwner, {response ->
                when (response) {
                    is NetworkResult.Success -> {
                        mainViewModel.login(email, password)
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(
                            requireContext(),
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is NetworkResult.Loading -> {
                    }
                }
            })

            mainViewModel.loginResult.observe(viewLifecycleOwner, {response ->
                when (response) {
                    is NetworkResult.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Welcome $email",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_registerFragment_to_productsFragment)
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
        }

        return binding.root
    }


}