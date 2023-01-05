package com.chrisojukwu.tallybookkeeping.ui.account

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.databinding.FragmentEmailCreateAccountBinding
import com.chrisojukwu.tallybookkeeping.domain.model.Provider
import com.chrisojukwu.tallybookkeeping.domain.model.User
import com.chrisojukwu.tallybookkeeping.ui.HomePageActivity
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.chrisojukwu.tallybookkeeping.utils.getRandomUserId
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EmailCreateAccountFragment : Fragment() {
    private val signInViewModel: SignInViewModel by activityViewModels()
    private lateinit var binding: FragmentEmailCreateAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEmailCreateAccountBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailFocusListener()

        passwordFocusListener()

        binding.closePageIcon.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        binding.textViewLogin.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_emailCreateAccountFragment_to_emailSignInPageFragment)
        }

        binding.buttonCreateAccount.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val confirmPassword = binding.editTextPasswordConfirm.text.toString()

            validateEmailPassword(email, password, confirmPassword)
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
        binding.editTextPasswordConfirm.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val password = binding.editTextPassword.text.toString()
                val confirmPassword = binding.editTextPasswordConfirm.text.toString()
                binding.confirmPasswordContainer.helperText =
                    validateConfirmPassword(password, confirmPassword)
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

    private fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        if (password != confirmPassword) {
            return "Passwords do not match"
        }
        return null
    }

    private fun validateEmailPassword(email: String, password: String, confirmPassword: String) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailContainer.helperText = "Invalid email"
        } else if (TextUtils.isEmpty(password)) {
            binding.passwordContainer.helperText = "Please enter password"
        } else if (password.length < 6) {
            binding.passwordContainer.helperText = "Password must be at least 6 characters"
        } else if (TextUtils.isEmpty(confirmPassword)) {
            binding.confirmPasswordContainer.helperText = "Please repeat password"
        } else if (password != confirmPassword) {
            binding.confirmPasswordContainer.helperText = "Passwords do not match"
        } else {
            initiateAccountCreation(email, password)
        }
    }

    private fun initiateAccountCreation(email: String, password: String) {
        lifecycleScope.launch {
            signInViewModel.createAccount(
                User(
                    email, password, getRandomUserId(), Provider.LOCAL, "", "",
                    "My Business Name", "Business Address", "", enabled = true
                )
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        findNavController().navigate(R.id.action_emailCreateAccountFragment_to_accountCreatedFragment)
                    }
                    is Result.Error -> {
                        binding.emailContainer.helperText = result.exception.message
                    }
                    is Result.Loading -> {}
                }
            }
        }

    }

}
