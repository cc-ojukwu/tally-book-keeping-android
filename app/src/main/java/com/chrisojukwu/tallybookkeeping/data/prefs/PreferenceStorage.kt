package com.chrisojukwu.tallybookkeeping.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_ONBOARDING
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_SIGNEDIN
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

/**
 * User preferences are stored here
 */
interface PreferenceStorage {

    suspend fun completeOnboarding(complete: Boolean)
    val onboardingCompleted: Flow<Boolean>

    suspend fun userSignInStatus(signInStatus: Boolean)
    val isUserSignedIn: Flow<Boolean>

    suspend fun authenticateUserToken(token: String)
    val authenticationToken: Flow<String>


}

@Singleton
class DataStorePreferenceStorage(
    private val dataStore: DataStore<Preferences>
) : PreferenceStorage {

    companion object {
        const val PREFS_NAME = "tally"
    }

    object PreferenceKeys {
        val PREF_ONBOARDING = booleanPreferencesKey("pref_onboarding")
        val PREF_SIGNEDIN = booleanPreferencesKey("pref_signedin")
        val PREF_TOKEN = stringPreferencesKey("pref_token")
    }

    override suspend fun completeOnboarding(complete: Boolean) {
        dataStore.edit {
            it[PREF_ONBOARDING] = complete
        }
    }
    override val onboardingCompleted: Flow<Boolean> =
        dataStore.data.map { it[PREF_ONBOARDING] ?: false }


    override suspend fun userSignInStatus(signInStatus: Boolean) {
        dataStore.edit {
            it[PREF_SIGNEDIN] = signInStatus
        }
    }
    override val isUserSignedIn: Flow<Boolean> =
    dataStore.data.map { it[PREF_SIGNEDIN] ?: true }


    override suspend fun authenticateUserToken(token: String) {
        dataStore.edit {
            it[PREF_TOKEN] = token
        }
    }
    override val authenticationToken: Flow<String> =
        dataStore.data.map { it[PREF_TOKEN] ?: "" }

}