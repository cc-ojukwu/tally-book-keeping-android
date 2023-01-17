package com.chrisojukwu.tallybookkeeping.data.source.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chrisojukwu.tallybookkeeping.data.asDMModel
import com.chrisojukwu.tallybookkeeping.data.source.local.RecordsLocalDataSource
import com.chrisojukwu.tallybookkeeping.data.source.remote.RecordsRemoteDataSource
import com.chrisojukwu.tallybookkeeping.data.toDMModel
import com.chrisojukwu.tallybookkeeping.domain.model.StringResponse
import com.chrisojukwu.tallybookkeeping.testShared.MainCoroutineRule
import com.chrisojukwu.tallybookkeeping.testShared.TestData.dummyExpense
import com.chrisojukwu.tallybookkeeping.testShared.TestData.dummyIncome
import com.chrisojukwu.tallybookkeeping.testShared.TestData.dummyNetworkExpense
import com.chrisojukwu.tallybookkeeping.testShared.TestData.dummyNetworkIncome
import com.chrisojukwu.tallybookkeeping.testShared.TestData.networkExpenseList
import com.chrisojukwu.tallybookkeeping.testShared.TestData.networkIncomeList
import kotlinx.coroutines.test.runTest
import com.chrisojukwu.tallybookkeeping.utils.Result
import kotlinx.coroutines.flow.first
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class RecordsRepositoryImplTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var remoteDataSource: RecordsRemoteDataSource

    @Mock
    private lateinit var localDataSource: RecordsLocalDataSource

    private lateinit var systemUnderTest: RecordsRepositoryImpl

    @Before
    fun setUp() {
        systemUnderTest = RecordsRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `assert that refreshIncomeData returns income data from remote source`() = runTest {
        `when` (remoteDataSource.getAllIncome()).thenReturn(
            Result.Success(networkIncomeList)
        )

        val response = systemUnderTest.getRemoteIncomeList().first()

        verify(remoteDataSource, times(1)).getAllIncome()
        when (response) {
            is Result.Success -> {
                val data = response.data
                assertThat(data, `is`(networkIncomeList.asDMModel()))
            }
            else -> {}
        }
    }

    @Test
    fun `assert that refreshExpenseData returns expense data from remote source`() = runTest {
        `when` (remoteDataSource.getAllExpense()).thenReturn(
            Result.Success(networkExpenseList)
        )

        val response = systemUnderTest.getRemoteExpenseList().first()

        verify(remoteDataSource, times(1)).getAllExpense()
        when (response) {
            is Result.Success -> {
                val data = response.data
                assertThat(data, `is`(networkExpenseList.toDMModel()))
            }
            else -> {}
        }
    }

    @Test
    fun `assert that insertIncome saves income data to remote database`() = runTest {
        `when` (remoteDataSource.saveIncome(dummyNetworkIncome)).thenReturn(
            Result.Success(StringResponse("ok"))
        )

        systemUnderTest.saveIncome(dummyIncome).first()

        verify(remoteDataSource, times(1)).saveIncome(dummyNetworkIncome)
    }

    @Test
    fun `assert that insertExpense saves expense data to remote database`() = runTest {
        `when` (remoteDataSource.saveExpense(dummyNetworkExpense)).thenReturn(
            Result.Success(StringResponse("ok"))
        )

        systemUnderTest.saveExpense(dummyExpense).first()

        verify(remoteDataSource, times(1)).saveExpense(dummyNetworkExpense)
    }
}