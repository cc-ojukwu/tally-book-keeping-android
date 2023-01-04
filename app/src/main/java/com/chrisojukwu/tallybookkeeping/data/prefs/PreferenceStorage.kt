package com.chrisojukwu.tallybookkeeping.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_BUSINESS_ADDRESS
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_BUSINESS_NAME
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_BUSINESS_PHONE
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_EMAIL
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_FIRST_NAME
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_LAST_NAME
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_ONBOARDING
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_SIGNEDIN
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_TOKEN
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_USER_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

/**
 * User preferences are stored here
 */
interface PreferenceStorage {

    suspend fun clearDatastore()

    suspend fun completeOnboarding(complete: Boolean)
    val onboardingCompleted: Flow<Boolean>

    suspend fun userSignInStatus(signInStatus: Boolean)
    val isUserSignedIn: Flow<Boolean>

    suspend fun authenticateUserToken(token: String)
    val authenticationToken: Flow<String?>

    suspend fun saveUserEmail(value: String)
    val getUserEmail: Flow<String?>

    suspend fun saveUserId(value: String)
    val getUserId: Flow<String?>

    suspend fun saveBusinessName(value: String)
    val getBusinessName: Flow<String?>

    suspend fun saveBusinessAddress(value: String)
    val getBusinessAddress: Flow<String?>

    suspend fun saveBusinessPhone(value: String)
    val getBusinessPhone: Flow<String?>

    suspend fun saveUserFirstName(value: String)
    val getUserFirstName: Flow<String?>

    suspend fun saveUserLastName(value: String)
    val getUserLastName: Flow<String?>


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
        val PREF_EMAIL = stringPreferencesKey("pref_email")
        val PREF_USER_ID = stringPreferencesKey("pref_user_id")
        val PREF_BUSINESS_NAME = stringPreferencesKey("pref_business_name")
        val PREF_BUSINESS_ADDRESS = stringPreferencesKey("pref_business_address")
        val PREF_BUSINESS_PHONE = stringPreferencesKey("pref_business_phone")
        val PREF_FIRST_NAME = stringPreferencesKey("pref_first_name")
        val PREF_LAST_NAME = stringPreferencesKey("pref_last_name")
    }

    override suspend fun clearDatastore() {
        dataStore.edit {
            it.clear()
        }
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

    override val authenticationToken: Flow<String?> =
        dataStore.data.map { it[PREF_TOKEN] }

    override suspend fun saveUserEmail(value: String) {
        dataStore.edit {
            it[PREF_EMAIL] = value
        }
    }

    override val getUserEmail: Flow<String?> =
        dataStore.data.map { it[PREF_EMAIL] }

    override suspend fun saveUserId(value: String) {
        dataStore.edit {
            it[PREF_USER_ID] = value
        }
    }

    override val getUserId: Flow<String?> =
        dataStore.data.map { it[PREF_USER_ID] }

    override suspend fun saveBusinessName(value: String) {
        dataStore.edit {
            it[PREF_BUSINESS_NAME] = value
        }
    }

    override val getBusinessName: Flow<String?> =
        dataStore.data.map { it[PREF_BUSINESS_NAME] }

    override suspend fun saveBusinessAddress(value: String) {
        dataStore.edit {
            it[PREF_BUSINESS_ADDRESS] = value
        }
    }

    override val getBusinessAddress: Flow<String?> =
        dataStore.data.map { it[PREF_BUSINESS_ADDRESS] }

    override suspend fun saveBusinessPhone(value: String) {
        dataStore.edit {
            it[PREF_BUSINESS_PHONE] = value
        }
    }

    override val getBusinessPhone: Flow<String?> =
        dataStore.data.map { it[PREF_BUSINESS_PHONE] }

    override suspend fun saveUserFirstName(value: String) {
        dataStore.edit {
            it[PREF_FIRST_NAME] = value
        }
    }

    override val getUserFirstName: Flow<String?> =
        dataStore.data.map { it[PREF_FIRST_NAME] }

    override suspend fun saveUserLastName(value: String) {
        dataStore.edit {
            it[PREF_LAST_NAME] = value
        }
    }

    override val getUserLastName: Flow<String?> =
        dataStore.data.map { it[PREF_LAST_NAME] }

}