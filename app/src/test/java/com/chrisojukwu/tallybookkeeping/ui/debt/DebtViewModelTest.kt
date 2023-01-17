package com.chrisojukwu.tallybookkeeping.ui.debt

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chrisojukwu.tallybookkeeping.domain.model.Provider
import com.chrisojukwu.tallybookkeeping.domain.model.StringResponse
import com.chrisojukwu.tallybookkeeping.domain.model.User
import com.chrisojukwu.tallybookkeeping.domain.usecase.*
import com.chrisojukwu.tallybookkeeping.testShared.*
import com.chrisojukwu.tallybookkeeping.ui.account.SignInViewModel
import com.chrisojukwu.tallybookkeeping.utils.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DebtViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var debtViewModel: DebtViewModel

    @Before
    fun setUp() {
        debtViewModel =
            DebtViewModel(
                GetLocalIncomeListUseCase(FakeRecordsRepository()),
                GetLocalExpenseListUseCase(FakeRecordsRepository())
            )
    }

    @Test
    fun `assert that receivable and payable sum is correct`() = runTest {

        val receivableSum = debtViewModel.receivableSum
        val payableSum = debtViewModel.payableSum

        assertThat(receivableSum.getOrAwaitValue(), `is`(4.00.toBigDecimal()))
        assertThat(payableSum.getOrAwaitValue(), `is`(5.00.toBigDecimal()))
    }

}