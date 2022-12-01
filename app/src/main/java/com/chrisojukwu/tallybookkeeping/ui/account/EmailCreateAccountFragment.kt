package com.chrisojukwu.tallybookkeeping.ui.account

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.databinding.FragmentEmailCreateAccountBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmailCreateAccountFragment : Fragment() {
    private val accountViewModel: AccountViewModel by activityViewModels()
    private lateinit var binding: FragmentEmailCreateAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEmailCreateAccountBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        // Pad the bottom of the RecyclerView so that the content scrolls up above the nav bar
//        binding.recyclerView.doOnApplyWindowInsets { v, insets, padding ->
//            val systemInsets = insets.getInsets(
//                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
//            )
//            v.updatePadding(bottom = padding.bottom + systemInsets.bottom)
//        }
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
        //var password = binding.editTextPassword.text.toString()
        //var confirmPassword = binding.editTextPasswordConfirm.text.toString()
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
        accountViewModel.createAccount(email, password).observe(viewLifecycleOwner) { result ->
            when (result) {
                (ServerResponse.SUCCESS) -> {
                    findNavController().navigate(R.id.action_emailCreateAccountFragment_to_accountCreatedFragment)
                }

                (ServerResponse.DUPLICATE) -> {
                    binding.emailContainer.helperText = "Email already registered"
                    //binding.textViewStatus.visibility = View.VISIBLE
                }
                else -> {
                    Snackbar.make(binding.buttonCreateAccount, R.string.registration_failed, Snackbar.LENGTH_LONG)
                        .setTextColor(Color.WHITE)
                        .setBackgroundTint(Color.RED)
                        .show()
                }
            }
        }

    }

}
