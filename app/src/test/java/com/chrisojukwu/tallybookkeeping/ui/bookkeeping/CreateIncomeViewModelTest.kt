package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chrisojukwu.tallybookkeeping.domain.model.Provider
import com.chrisojukwu.tallybookkeeping.domain.model.User
import com.chrisojukwu.tallybookkeeping.domain.usecase.CreateNewAccountUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.SaveIncomeUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.SignInWithEmailUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.SignInWithGoogleUseCase
import com.chrisojukwu.tallybookkeeping.testShared.FakePreferencesStorage
import com.chrisojukwu.tallybookkeeping.testShared.FakeRecordsRepository
import com.chrisojukwu.tallybookkeeping.testShared.MainCoroutineRule
import com.chrisojukwu.tallybookkeeping.testShared.TestData
import com.chrisojukwu.tallybookkeeping.ui.account.SignInViewModel
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CreateIncomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var createIncomeViewModel: CreateIncomeViewModel

    @Before
    fun setUp() {
        createIncomeViewModel =
            CreateIncomeViewModel(
                SaveIncomeUseCase(FakeRecordsRepository())
            )
    }
}