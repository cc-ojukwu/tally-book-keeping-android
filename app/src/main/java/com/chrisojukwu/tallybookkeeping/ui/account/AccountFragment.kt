package com.chrisojukwu.tallybookkeeping.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.databinding.FragmentAccountBinding
import com.chrisojukwu.tallybookkeeping.domain.model.OldNewPassword
import com.chrisojukwu.tallybookkeeping.domain.model.Provider
import com.chrisojukwu.tallybookkeeping.domain.model.User
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private val accountViewModel: AccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = accountViewModel
        }
        requireActivity().window.statusBarColor = requireActivity().resources.getColor(R.color.background_color1, null)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutAccountEmail.setOnClickListener {
            openEmailDialog()
        }
        binding.layoutAccountName.setOnClickListener {
            openAccountNameDialog()
        }
        binding.layoutAccountPassword.setOnClickListener {
            openPasswordDialog()
        }
        binding.layoutBusinessName.setOnClickListener {
            openBusinessNameDialog()
        }
        binding.layoutBusinessAddress.setOnClickListener {
            openBusinessAddressDialog()
        }
        binding.layoutBusinessPhone.setOnClickListener {
            openBusinessPhoneDialog()
        }
        binding.layoutSignOut.setOnClickListener {
            openSignOutDialog()
        }
    }

    private fun openEmailDialog() {
        val emailDialog = BottomSheetDialog(requireContext())

        emailDialog.setContentView(R.layout.update_email_bottomsheet)

        val editTextEmail = emailDialog.findViewById<EditText>(R.id.edit_text_account_email)
        val saveButton = emailDialog.findViewById<Button>(R.id.change_account_email_button_done)

        saveButton?.setOnClickListener {
            val email = editTextEmail?.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail?.error = "Invalid input"
            } else {
                initiateChangeEmail(email)
                emailDialog.dismiss()
            }
        }

        emailDialog.setCancelable(true)
        emailDialog.show()
    }

    private fun initiateChangeEmail(email: String) {
        lifecycleScope.launch {
            accountViewModel.changeEmail(
                User(
                    email = email, "", "", Provider.LOCAL
                )
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), "Email updated!", Toast.LENGTH_LONG).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Error, try again later", Toast.LENGTH_LONG).show()
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }

    private fun openAccountNameDialog() {
        val accountNameDialog = BottomSheetDialog(requireContext())

        accountNameDialog.setContentView(R.layout.update_account_name_bottomsheet)

        val editTextFirstName = accountNameDialog.findViewById<EditText>(R.id.edit_text_account_first_name)
        val editTextLastName = accountNameDialog.findViewById<EditText>(R.id.edit_text_account_last_name)
        val saveButton = accountNameDialog.findViewById<Button>(R.id.change_account_name_button_done)

        saveButton?.setOnClickListener {
            val firstName = editTextFirstName?.text.toString()
            val lastName = editTextLastName?.text.toString()
            if (firstName.isBlank()) {
                editTextFirstName?.error = "Invalid input"
            } else if (lastName.isBlank()) {
                editTextLastName?.error = "Invalid input"
            } else {
                initiateChangeName(firstName, lastName)
                accountNameDialog.dismiss()
            }
        }

        accountNameDialog.setCancelable(true)
        accountNameDialog.show()
    }

    private fun initiateChangeName(firstName: String, lastName: String) {
        lifecycleScope.launch {
            accountViewModel.updateUserInfo(
                User(
                    email = "",
                    password = "",
                    userId = "",
                    provider = Provider.LOCAL,
                    firstName = firstName,
                    lastName = lastName,
                    businessName = accountViewModel.businessName.value!!,
                    businessAddress = accountViewModel.businessAddress.value!!,
                    businessPhone = accountViewModel.businessPhone.value!!
                )
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), "Updated!", Toast.LENGTH_LONG).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Error, try again later", Toast.LENGTH_LONG).show()
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }

    private fun openPasswordDialog() {
        val passwordDialog = BottomSheetDialog(requireContext())

        passwordDialog.setContentView(R.layout.change_password_bottomsheet)

        val oldPasswordText = passwordDialog.findViewById<EditText>(R.id.edit_text_old_password)
        val newPasswordText = passwordDialog.findViewById<EditText>(R.id.edit_text_new_password)
        val saveButton = passwordDialog.findViewById<Button>(R.id.change_password_button_done)

        saveButton?.setOnClickListener {
            val oldPassword = oldPasswordText?.text.toString()
            val newPassword = newPasswordText?.text.toString()
            if (newPassword.length < 6 || newPassword.isBlank()) {
                newPasswordText?.error = "Invalid input"
            } else {
                initiateChangePassword(oldPassword, newPassword)
                passwordDialog.dismiss()
            }
        }

        passwordDialog.setCancelable(true)
        passwordDialog.show()
    }

    private fun initiateChangePassword(oldPassword: String, newPassword: String) {
        lifecycleScope.launch {
            accountViewModel.changePassword(OldNewPassword(oldPassword, newPassword))
                .collect { result ->
                    when (result) {
                        is Result.Success -> {
                            Toast.makeText(requireContext(), "Password changed", Toast.LENGTH_LONG).show()
                        }
                        is Result.Error -> {
                            Toast.makeText(requireContext(), "Error, try again later", Toast.LENGTH_LONG).show()
                        }
                        is Result.Loading -> {}
                    }
                }
        }
    }

    private fun openBusinessNameDialog() {
        val businessNameDialog = BottomSheetDialog(requireContext())

        businessNameDialog.setContentView(R.layout.update_business_name_bottomsheet)

        val businessNameText = businessNameDialog.findViewById<EditText>(R.id.edit_text_business_name)
        val saveButton = businessNameDialog.findViewById<Button>(R.id.update_business_name_button_done)

        saveButton?.setOnClickListener {
            val businessName = businessNameText?.text.toString()
            if (businessName.isBlank()) {
                businessNameText?.error = "Invalid input"
            } else {
                initiateBusinessNameUpdate(businessName)
                businessNameDialog.dismiss()
            }
        }

        businessNameDialog.setCancelable(true)
        businessNameDialog.show()
    }

    private fun initiateBusinessNameUpdate(businessName: String) {
        lifecycleScope.launch {
            accountViewModel.updateUserInfo(
                User(
                    email = "",
                    password = "",
                    userId = "",
                    provider = Provider.LOCAL,
                    firstName = accountViewModel.accountFirstName.value!!,
                    lastName = accountViewModel.accountLastName.value!!,
                    businessName = businessName,
                    businessAddress = accountViewModel.businessAddress.value!!,
                    businessPhone = accountViewModel.businessPhone.value!!
                )
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), "updated", Toast.LENGTH_LONG).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Error, try again later", Toast.LENGTH_LONG).show()
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }


    private fun openBusinessAddressDialog() {
        val businessAddressDialog = BottomSheetDialog(requireContext())

        businessAddressDialog.setContentView(R.layout.update_business_address_bottomsheet)

        val businessAddressText = businessAddressDialog.findViewById<EditText>(R.id.edit_text_business_address)
        val saveButton = businessAddressDialog.findViewById<Button>(R.id.update_business_address_button_done)

        saveButton?.setOnClickListener {
            val businessAddress = businessAddressText?.text.toString()
            if (businessAddress.isBlank()) {
                businessAddressText?.error = "Invalid input"
            } else {
                initiateBusinessAddressUpdate(businessAddress)
                businessAddressDialog.dismiss()
            }
        }
        businessAddressDialog.setCancelable(true)
        businessAddressDialog.show()
    }

    private fun initiateBusinessAddressUpdate(businessAddress: String) {
        lifecycleScope.launch {
            accountViewModel.updateUserInfo(
                User(
                    email = "",
                    password = "",
                    userId = "",
                    provider = Provider.LOCAL,
                    firstName = accountViewModel.accountFirstName.value!!,
                    lastName = accountViewModel.accountLastName.value!!,
                    businessName = accountViewModel.businessAddress.value!!,
                    businessAddress = businessAddress,
                    businessPhone = accountViewModel.businessPhone.value!!
                )
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), "updated", Toast.LENGTH_LONG).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Error, try again later", Toast.LENGTH_LONG).show()
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }

    private fun openBusinessPhoneDialog() {
        val businessPhoneDialog = BottomSheetDialog(requireContext())

        businessPhoneDialog.setContentView(R.layout.update_business_phone_bottomsheet)

        val businessPhoneText = businessPhoneDialog.findViewById<EditText>(R.id.edit_text_business_phone)
        val saveButton = businessPhoneDialog.findViewById<Button>(R.id.update_business_phone_button_done)

        saveButton?.setOnClickListener {
            val businessPhone = businessPhoneText?.text.toString()
            if (businessPhone.isBlank()) {
                businessPhoneText?.error = "Invalid input"
            } else {
                initiateBusinessPhoneUpdate(businessPhone)
                businessPhoneDialog.dismiss()
            }
        }
        businessPhoneDialog.setCancelable(true)
        businessPhoneDialog.show()
    }

    private fun initiateBusinessPhoneUpdate(businessPhone: String) {
        lifecycleScope.launch {
            accountViewModel.updateUserInfo(
                User(
                    email = "",
                    password = "",
                    userId = "",
                    provider = Provider.LOCAL,
                    firstName = accountViewModel.accountFirstName.value!!,
                    lastName = accountViewModel.accountLastName.value!!,
                    businessName = accountViewModel.businessAddress.value!!,
                    businessAddress = accountViewModel.businessAddress.value!!,
                    businessPhone = businessPhone
                )
            ).collect { result ->
                when (result) {
                    is Result.Success -> {
                        Toast.makeText(requireContext(), "updated", Toast.LENGTH_LONG).show()
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Error, try again later", Toast.LENGTH_LONG).show()
                    }
                    is Result.Loading -> {}
                }
            }
        }
    }

    private fun openSignOutDialog() {
        val signOutDialog = BottomSheetDialog(requireContext())

        signOutDialog.setContentView(R.layout.sign_out_bottomsheet)

        val yesButton = signOutDialog.findViewById<TextView>(R.id.sign_out_yes_button)
        val noButton = signOutDialog.findViewById<TextView>(R.id.sign_out_no_button)

        yesButton?.setOnClickListener {
            initiateSignOut()
            signOutDialog.dismiss()
        }
        noButton?.setOnClickListener {
            signOutDialog.dismiss()
        }

        signOutDialog.setCancelable(true)
        signOutDialog.show()
    }

    private fun initiateSignOut() {
        lifecycleScope.launch {
            accountViewModel.signOut()
                .collect {
                    if (it) {
                        val intent =
                            Intent(this@AccountFragment.requireContext(), SignInActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "Couldn't complete sign out", Toast.LENGTH_LONG).show()
                    }
                }

        }
    }

}