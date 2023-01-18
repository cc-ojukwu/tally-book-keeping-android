package com.chrisojukwu.tallybookkeeping.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import com.chrisojukwu.tallybookkeeping.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch (ioDispatcher) {
            preferenceStorage.completeOnboarding(true)
        }
    }
}