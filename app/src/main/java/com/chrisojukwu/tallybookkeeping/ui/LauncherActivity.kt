package com.chrisojukwu.tallybookkeeping.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.chrisojukwu.tallybookkeeping.ui.account.SignInActivity
import com.chrisojukwu.tallybookkeeping.ui.onboarding.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val launchViewModel: LaunchViewModel by viewModels()

        lifecycleScope.launch {
            //run the block below once lifecycle state enters onStart
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launchViewModel.launchDestination.collect { action ->
                    when (action) {
                        is NavigationAction.NavigateToHomePageActivityAction -> startActivity(
                            Intent(this@LauncherActivity, HomePageActivity::class.java)
                        )
                        is NavigationAction.NavigateToNextCondition -> {
                            launchViewModel.alternateDestination.collect { alternateAction ->
                                if (alternateAction == NavigationAction.NavigateToSignInActivityAction) {
                                    startActivity(
                                        Intent(this@LauncherActivity, SignInActivity::class.java)
                                    )
                                } else {
                                    startActivity(
                                        Intent(this@LauncherActivity, OnboardingActivity::class.java)
                                    )
                                }

                            }
                        }
                        else -> {}
                    }
                    finish()
                }


            }
        }
    }
}