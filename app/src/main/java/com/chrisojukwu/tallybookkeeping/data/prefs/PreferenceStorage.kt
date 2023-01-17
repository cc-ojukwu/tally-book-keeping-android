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
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_SIGNED_IN
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_TOKEN
import com.chrisojukwu.tallybookkeeping.data.prefs.DataStorePreferenceStorage.PreferenceKeys.PREF_USER_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * User preferences are stored here
 */
interface PreferenceStorage {

    suspend fun clearDatastore()

    suspend fun completeOnboarding(isComplete: Boolean)
    val onboardingCompleted: Flow<Boolean>

    suspend fun userSignInStatus(signIn: Boolean)
    val isUserSignedIn: Flow<Boolean>

    suspend fun saveToken(token: String)
    val getAuthenticationToken: Flow<String>

    suspend fun saveUserEmail(email: String)
    suspend fun getUserEmail(): Flow<String>

    suspend fun saveUserId(id: String)
    suspend fun getUserId(): Flow<String>

    suspend fun saveBusinessName(businessName: String)
    suspend fun getBusinessName(): Flow<String>

    suspend fun saveBusinessAddress(businessAddress: String)
    suspend fun getBusinessAddress(): Flow<String>

    suspend fun saveBusinessPhone(businessPhone: String)
    suspend fun getBusinessPhone(): Flow<String>

    suspend fun saveUserFirstName(firstName: String)
    suspend fun getUserFirstName(): Flow<String>

    suspend fun saveUserLastName(lastName: String)
    suspend fun getUserLastName(): Flow<String>

}

@Singleton
class DataStorePreferenceStorage @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceStorage {

    companion object {
        const val PREFS_NAME = "tally_bookkeeping"
    }

    object PreferenceKeys {
        val PREF_ONBOARDING = booleanPreferencesKey("pref_onboarding")
        val PREF_SIGNED_IN = booleanPreferencesKey("pref_signedin")
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
            Timber.d("clearing datastore")
            it.remove(PREF_TOKEN)
            it.remove(PREF_SIGNED_IN)
            it.remove(PREF_EMAIL)
            it.remove(PREF_USER_ID)
            it.remove(PREF_BUSINESS_NAME)
            it.remove(PREF_BUSINESS_ADDRESS)
            it.remove(PREF_BUSINESS_PHONE)
            it.remove(PREF_FIRST_NAME)
            it.remove(PREF_LAST_NAME)
        }
    }

    override suspend fun completeOnboarding(isComplete: Boolean) {
        dataStore.edit {
            it[PREF_ONBOARDING] = isComplete
        }
    }

    override val onboardingCompleted: Flow<Boolean> =
        dataStore.data.map {
            it[PREF_ONBOARDING] ?: false
        }

    override suspend fun userSignInStatus(signIn: Boolean) {
        dataStore.edit {
            it[PREF_SIGNED_IN] = signIn
        }
    }

    override val isUserSignedIn: Flow<Boolean> =
        dataStore.data.map { it[PREF_SIGNED_IN] ?: false }


    override suspend fun saveToken(token: String) {
        dataStore.edit {
            Timber.e(token)
            it[PREF_TOKEN] = token
        }
    }

    override val getAuthenticationToken: Flow<String> =
        dataStore.data.map {
            it[PREF_TOKEN] ?: ""
        }

    override suspend fun saveUserEmail(email: String) {
        dataStore.edit {
            it[PREF_EMAIL] = email
        }
    }

    override suspend fun getUserEmail(): Flow<String> =
        dataStore.data.map {
            it[PREF_EMAIL] ?: ""
        }

    override suspend fun saveUserId(id: String) {
        dataStore.edit {
            it[PREF_USER_ID] = id
        }
    }

    override suspend fun getUserId(): Flow<String> =
        dataStore.data.map { it[PREF_USER_ID] ?: "" }

    override suspend fun saveBusinessName(businessName: String) {
        dataStore.edit {
            it[PREF_BUSINESS_NAME] = businessName
        }
    }

    override suspend fun getBusinessName(): Flow<String> =
        dataStore.data
            .map {
                it[PREF_BUSINESS_NAME] ?: "My Business Name"
            }


    override suspend fun saveBusinessAddress(businessAddress: String) {
        dataStore.edit {
            it[PREF_BUSINESS_ADDRESS] = businessAddress
        }
    }

    override suspend fun getBusinessAddress(): Flow<String> =
        dataStore.data.map {
            it[PREF_BUSINESS_ADDRESS] ?: ""
        }

    override suspend fun saveBusinessPhone(businessPhone: String) {
        dataStore.edit {
            it[PREF_BUSINESS_PHONE] = businessPhone
        }
    }

    override suspend fun getBusinessPhone(): Flow<String> =
        dataStore.data.map {
            it[PREF_BUSINESS_PHONE] ?: ""
        }

    override suspend fun saveUserFirstName(firstName: String) {
        dataStore.edit {
            it[PREF_FIRST_NAME] = firstName
        }
    }

    override suspend fun getUserFirstName(): Flow<String> =
        dataStore.data.map {
            it[PREF_FIRST_NAME] ?: ""
        }

    override suspend fun saveUserLastName(lastName: String) {
        dataStore.edit {
            it[PREF_LAST_NAME] = lastName
        }
    }

    override suspend fun getUserLastName(): Flow<String> =
        dataStore.data.map {
            it[PREF_LAST_NAME] ?: ""
        }

}