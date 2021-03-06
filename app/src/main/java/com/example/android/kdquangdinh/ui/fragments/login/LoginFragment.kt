package com.example.android.kdquangdinh.ui.fragments.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.android.kdquangdinh.R
import com.example.android.kdquangdinh.databinding.FragmentLoginBinding
import com.example.android.kdquangdinh.databinding.FragmentRegisterBinding
import com.example.android.kdquangdinh.models.Product
import com.example.android.kdquangdinh.util.NetworkResult
import com.example.android.kdquangdinh.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
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
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEdittext.text.toString().trim()
            val password = binding.passwordEdittext.text.toString().trim()

            val isEmailAndPasswordValid = mainViewModel.validateEmailAndPassword(email, password)
            if (!isEmailAndPasswordValid)
                return@setOnClickListener

            //TO-DO validate email and password
            mainViewModel.login(email, password)
            mainViewModel.loginResult.observe(viewLifecycleOwner, {response ->
                when (response) {
                    is NetworkResult.Success -> {
                        Toast.makeText(
                            requireContext(),
                            "Welcome $email",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_loginFragment_to_productsFragment)
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

        }

        mainViewModel.error.observe(viewLifecycleOwner, { error ->
            showToast(error)
        })

        return binding.root
    }

    private fun showToast(message: String?) {
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }



}