package com.chrisojukwu.tallybookkeeping.ui.account

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.IntentSender
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.chrisojukwu.tallybookkeeping.R
import com.chrisojukwu.tallybookkeeping.databinding.FragmentSignInBinding
import com.chrisojukwu.tallybookkeeping.ui.HomePageActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private val accountViewModel: AccountViewModel by activityViewModels()
    private lateinit var gso: GoogleSignInOptions
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    private lateinit var oneTapClient: SignInClient
    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var signInRequest: BeginSignInRequest

    private val oneTapResult: ActivityResultLauncher<IntentSenderRequest> =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) Timber.d("result_ok")
            if (result.resultCode == RESULT_CANCELED) Timber.d("result_cancelled")
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential.googleIdToken
                if (idToken != null) {
                    Timber.d("idToken acquired")
                    Log.d("token from google", idToken)
                    //Use idToken to authenticate with backend here
                    accountViewModel.googleLogin(idToken).observe(viewLifecycleOwner) { returnValue ->
                        if (returnValue == ServerResponse.SUCCESS) {
                            val intent = Intent(this@SignInFragment.requireContext(), HomePageActivity::class.java)
                            startActivity(intent)
                        } else {
                            Snackbar.make(binding.root, R.string.show_failure, Snackbar.LENGTH_LONG)
                                .setTextColor(Color.WHITE)
                                .setBackgroundTint(Color.RED)
                                .show()
                        }
                    }

                } else Timber.d("no idToken found")
            } catch (e: ApiException) {
                when (e.statusCode) {
                    CommonStatusCodes.CANCELED -> {
                        Timber.e("status code - cancelled ")
                    }
                    CommonStatusCodes.NETWORK_ERROR -> {
                        Timber.e("status code - network error")
                    }
                }
            }

        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity?)!!.supportActionBar?.hide()
        binding = FragmentSignInBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewEmailSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_emailSignInPageFragment)
        }

        binding.textViewRegister.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_emailCreateAccountFragment)
        }

        oneTapClient = Identity.getSignInClient(this.requireActivity())

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("1053376402234-4jnl17jv924e26s2b8c38an7oqjvqevd.apps.googleusercontent.com")
                    // Show previously signed in accounts on device.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()

        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("1053376402234-4jnl17jv924e26s2b8c38an7oqjvqevd.apps.googleusercontent.com")
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        binding.textViewGoogleSignIn.setOnClickListener {
            signInWithGoogle()
        }

        //disable back button for this fragment
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // With blank your fragment BackPressed will be disabled.
        }
    }

    private fun signInWithGoogle() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this.requireActivity()) { result: BeginSignInResult ->
                try {
                    val isr = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(isr)
                } catch (e: IntentSender.SendIntentException) {
                    Timber.e("Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this.requireActivity()) { e ->
                // No previously signed in accounts found. Try all google accounts in device.
                Timber.d(e.localizedMessage)
                signUpWithGoogle()
            }
    }

    private fun signUpWithGoogle() {
        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener() { result: BeginSignInResult ->
                try {
                    val isr = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    oneTapResult.launch(isr)
                } catch (e: IntentSender.SendIntentException) {
                    Timber.e("Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener() { e: Exception ->
                // No Google account found on device
                Timber.d(e.localizedMessage)
                Timber.e("Didn't find any google account on device")
            }
    }



}