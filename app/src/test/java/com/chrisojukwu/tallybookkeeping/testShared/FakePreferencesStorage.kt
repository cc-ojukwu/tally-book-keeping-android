package com.chrisojukwu.tallybookkeeping.testShared

import com.chrisojukwu.tallybookkeeping.data.prefs.PreferenceStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class FakePreferencesStorage(): PreferenceStorage {
    override suspend fun clearDatastore() {
        TODO("Not yet implemented")
    }

    override suspend fun completeOnboarding(complete: Boolean) {
        TODO("Not yet implemented")
    }

    override val onboardingCompleted: Flow<Boolean>
        get() = TODO("Not yet implemented")

    override suspend fun userSignInStatus(signInStatus: Boolean) {
        TODO("Not yet implemented")
    }

    override val isUserSignedIn: Flow<Boolean>
        get() = TODO("Not yet implemented")

    override suspend fun saveToken(token: String) {
        TODO("Not yet implemented")
    }

    override val getAuthenticationToken: Flow<String>
        get() = TODO("Not yet implemented")

    override suspend fun saveUserEmail(value: String) {
    }

    override suspend fun getUserEmail(): Flow<String> = flow {
        emit(TestData.email)
    }

    override suspend fun saveUserId(value: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserId(): Flow<String> {
        TODO("Not yet implemented")
    }

    override suspend fun saveBusinessName(value: String) {
        TestData.savedBusinessName = value
    }

    override suspend fun getBusinessName(): Flow<String> = flow {
        emit(TestData.businessName)
    }

    override suspend fun saveBusinessAddress(value: String) {
        TestData.savedBusinessAddress = value
    }

    override suspend fun getBusinessAddress(): Flow<String> = flow {
        emit(TestData.businessAddress)
    }

    override suspend fun saveBusinessPhone(value: String) {
        TestData.savedBusinessPhone = value
    }

    override suspend fun getBusinessPhone(): Flow<String> = flow {
        emit(TestData.businessPhone)
    }

    override suspend fun saveUserFirstName(value: String) {
        TestData.savedFirstName = value
    }

    override suspend fun getUserFirstName(): Flow<String> = flow {
        emit(TestData.firstName)
    }

    override suspend fun saveUserLastName(value: String) {
        TestData.savedLastName = value
    }

    override suspend fun getUserLastName(): Flow<String> = flow {
        emit(TestData.lastName)
    }
}