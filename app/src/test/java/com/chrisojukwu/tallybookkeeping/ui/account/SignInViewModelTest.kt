package com.chrisojukwu.tallybookkeeping.ui.account

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chrisojukwu.tallybookkeeping.domain.model.Provider
import com.chrisojukwu.tallybookkeeping.domain.model.StringResponse
import com.chrisojukwu.tallybookkeeping.domain.model.User
import com.chrisojukwu.tallybookkeeping.domain.usecase.*
import com.chrisojukwu.tallybookkeeping.testShared.FakePreferencesStorage
import com.chrisojukwu.tallybookkeeping.testShared.FakeRecordsRepository
import com.chrisojukwu.tallybookkeeping.testShared.MainCoroutineRule
import com.chrisojukwu.tallybookkeeping.testShared.TestData
import com.chrisojukwu.tallybookkeeping.testShared.TestData.businessAddress
import com.chrisojukwu.tallybookkeeping.testShared.TestData.businessName
import com.chrisojukwu.tallybookkeeping.testShared.TestData.businessPhone
import com.chrisojukwu.tallybookkeeping.testShared.TestData.firstName
import com.chrisojukwu.tallybookkeeping.testShared.TestData.lastName
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import com.chrisojukwu.tallybookkeeping.utils.Result
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SignInViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var signInViewModel: SignInViewModel

    @Before
    fun setUp() {
        signInViewModel =
            SignInViewModel(
                FakePreferencesStorage(),
                CreateNewAccountUseCase(FakeRecordsRepository()),
                SignInWithEmailUseCase(FakeRecordsRepository()),
                SignInWithGoogleUseCase(FakeRecordsRepository()),
                ResetPasswordUseCase(FakeRecordsRepository())
            )
    }

    @Test
    fun `assert that new user account is created correctly`() = runTest {
        val user = User(
            TestData.email, "4r949nf", "user-49u49", Provider.LOCAL, firstName, lastName,
            businessName, businessAddress, businessPhone, "ROLE_USER", true
        )

        val result = signInViewModel.createAccount(user).first()

        assertThat(result, `is`(Result.Success(StringResponse("created"))))
    }
}