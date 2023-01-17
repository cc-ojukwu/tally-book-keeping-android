package com.chrisojukwu.tallybookkeeping.ui.account

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.databinding.FragmentForgotPasswordBinding
import com.chrisojukwu.tallybookkeeping.domain.model.ResetPasswordEmail
import com.chrisojukwu.tallybookkeeping.utils.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val signInViewModel: SignInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = signInViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backIcon.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.buttonResetPassword.setOnClickListener {
            val email = binding.editTextEmail.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editTextEmail.error = "Invalid email"
            }else {
                lifecycleScope.launch {
                    signInViewModel.setIsLoading(true)
                    signInViewModel.resetPassword(ResetPasswordEmail(email))
                        .collect { result ->
                            when (result) {
                                is Result.Success -> {
                                    signInViewModel.setIsLoading(false)
                                    Toast.makeText(requireContext(), "Password reset. Check your email", Toast.LENGTH_LONG).show()
                                }
                                is Result.Error -> {
                                    signInViewModel.setIsLoading(false)
                                    Toast.makeText(requireContext(), "Email not registered", Toast.LENGTH_LONG).show()
                                }
                                is Result.Loading -> {}
                            }
                        }
                }
            }
        }

    }
}