package com.chrisojukwu.tallybookkeeping.ui.account

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chrisojukwu.tallybookkeeping.domain.usecase.*
import com.chrisojukwu.tallybookkeeping.testShared.*
import com.chrisojukwu.tallybookkeeping.ui.bookkeeping.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AccountViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var accountViewModel: AccountViewModel

    @Before
    fun setUp() {
        accountViewModel =
            AccountViewModel(
                FakePreferencesStorage(),
                ChangeEmailUseCase(FakeRecordsRepository()),
                ChangePasswordUseCase(FakeRecordsRepository()),
                UpdateUserInfoUseCase(FakeRecordsRepository()),
                mainCoroutineRule.testDispatcher
            )
    }

    @Test
    fun `assert that all account info is loaded`() = runTest {

        val email = accountViewModel.accountEmail
        val firstName = accountViewModel.accountFirstName
        val lastName = accountViewModel.accountLastName
        val businessName = accountViewModel.businessName
        val businessAddress = accountViewModel.businessAddress
        val businessPhone = accountViewModel.businessPhone


        assertThat(email.getOrAwaitValue(), `is`(TestData.email))
        assertThat(firstName.getOrAwaitValue(), `is`(TestData.firstName))
        assertThat(lastName.getOrAwaitValue(), `is`(TestData.lastName))
        assertThat(businessName.getOrAwaitValue(), `is`(TestData.businessName))
        assertThat(businessAddress.getOrAwaitValue(), `is`(TestData.businessAddress))
        assertThat(businessPhone.getOrAwaitValue(), `is`(TestData.businessPhone))
    }

    @Test
    fun `assert that user info is saved to datastore`() = runTest {
        accountViewModel.updateUserInfo(TestData.user)

        assertThat(TestData.savedFirstName, `is`(TestData.firstName))
        assertThat(TestData.savedLastName, `is`(TestData.lastName))
        assertThat(TestData.savedBusinessName, `is`(TestData.businessName))
        assertThat(TestData.savedBusinessAddress, `is`(TestData.businessAddress))
        assertThat(TestData.savedBusinessPhone, `is`(TestData.businessPhone))
    }

}