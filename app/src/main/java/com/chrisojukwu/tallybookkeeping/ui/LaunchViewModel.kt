package com.chrisojukwu.tallybookkeeping.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class LaunchViewModel @Inject constructor(
    preferenceStorage: PreferenceStorage
) : ViewModel() {

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

