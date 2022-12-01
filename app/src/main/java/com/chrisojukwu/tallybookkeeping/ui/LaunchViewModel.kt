package com.chrisojukwu.tallybookkeeping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * Logic to pick a screen to navigate to
 */
@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : ViewModel() {

    //var launchDestination: NavigationAction = NavigationAction.NavigateToSignInActivityAction
    //lateinit var launchDestination: NavigationAction

    //val launchDestination = chooseDestination()
/*
    private fun chooseDestination(): NavigationAction {
        var destination: NavigationAction = NavigationAction.NavigateToSignInActivityAction
        viewModelScope.launch {
            var onboardingCompleted = false
            var isSignedInStatus = false
//            preferenceStorage.onboardingCompleted.collect { onboarding ->
//                onboardingCompleted = onboarding
//            }
//            preferenceStorage.isUserSignedIn.collect { isSignedIn ->
//                isSignedInStatus = isSignedIn
//            }
            if (isSignedInStatus) {
                destination = NavigationAction.NavigateToMainActivityAction
            } else if (!onboardingCompleted) {
               destination = NavigationAction.NavigateToOnboardingAction
            } else  destination = NavigationAction.NavigateToSignInActivityAction
        }
        return destination
    }

 */

    val launchDestination = preferenceStorage.isUserSignedIn.map { isSignedIn ->
        if (isSignedIn) {
            NavigationAction.NavigateToHomePageActivityAction
        } else {
            NavigationAction.NavigateToNextCondition
        }
    }.stateIn(viewModelScope, Eagerly, null)

    val alternateDestination = preferenceStorage.onboardingCompleted.map { onboarded ->
        if (onboarded) {
            NavigationAction.NavigateToSignInActivityAction
        } else NavigationAction.NavigateToOnboardingAction
    }.stateIn(viewModelScope, Eagerly, null)
}

sealed class NavigationAction {
    object NavigateToOnboardingAction : NavigationAction()
    object NavigateToSignInActivityAction : NavigationAction()
    object NavigateToHomePageActivityAction : NavigationAction()
    object NavigateToNextCondition : NavigationAction()
}

