package com.chrisojukwu.tallybookkeeping.ui.account

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.databinding.FragmentEmailSignInPageBinding
import com.chrisojukwu.tallybookkeeping.ui.HomePageActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailSignInPageFragment : Fragment() {

    private lateinit var binding: FragmentEmailSignInPageBinding
    private val accountViewModel: SignInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEmailSignInPageBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = accountViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding.editTextEmail.requestFocus()

        binding.closePageIcon.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        binding.textViewForgotPassword.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_emailSignInPageFragment_to_forgotPasswordFragment)
        }

        binding.textViewSignUp.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_emailSignInPageFragment_to_emailCreateAccountFragment)
        }

        emailFocusListener()

        passwordFocusListener()

        binding.buttonSignIn.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            validateEmailPassword(email, password)
        }
    }

    private fun emailFocusListener() {
        binding.editTextEmail.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val email = binding.editTextEmail.text.toString()
                binding.emailContainer.helperText = validateEmail(email)
            }
        }
    }

    private fun passwordFocusListener() {
        binding.editTextPassword.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val password = binding.editTextPassword.text.toString()
                binding.passwordContainer.helperText = validatePassword(password)
            }
        }
    }

    private fun validateEmail(email: String): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return "Invalid email"
        }
        return null
    }

    private fun validatePassword(password: String): String? {
        if (password.length < 6) {
            return "Password must be at least 6 characters"
        }
        return null
    }

    private fun validateEmailPassword(email: String, password: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailContainer.helperText = "Invalid email"
        } else if (TextUtils.isEmpty(password)) {
            binding.passwordContainer.helperText = "Please enter password"
        } else if (password.length < 6) {
            binding.passwordContainer.helperText = "Password must be at least 6 characters"
        } else {
            signIn(email, password)
        }
    }

    private fun signIn(email: String, password: String) {

        accountViewModel.login(email, password).observe(viewLifecycleOwner) { result ->
            when (result) {
                (ServerResponse.SUCCESS) -> {
                    println("isLoading-- ${accountViewModel._isLoading.value.toString()}")
                    val intent = Intent(this@EmailSignInPageFragment.requireContext(), HomePageActivity::class.java)
                    startActivity(intent)
                }
                (ServerResponse.UNAVAILABLE) -> {
                    binding.emailContainer.helperText = "Sign in failed. Server unreachable"
                }
                else -> {
                    binding.emailContainer.helperText = "Sign in failed. Invalid credentials"
                }
            }

        }
    }

}