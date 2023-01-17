package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chrisojukwu.tallybookkeeping.domain.model.RecordHolder
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetExpenseListUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetIncomeListUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetLocalExpenseListUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetLocalIncomeListUseCase
import com.chrisojukwu.tallybookkeeping.testShared.FakeRecordsRepository
import com.chrisojukwu.tallybookkeeping.testShared.MainCoroutineRule
import com.chrisojukwu.tallybookkeeping.testShared.TestData
import com.chrisojukwu.tallybookkeeping.testShared.getOrAwaitValue
import org.junit.Assert
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AllRecordsViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var allRecordsViewModel: AllRecordsViewModel

    @Before
    fun setUp() {
        allRecordsViewModel =
            AllRecordsViewModel(
                GetLocalIncomeListUseCase(FakeRecordsRepository()),
                GetLocalExpenseListUseCase(FakeRecordsRepository())
            )
    }

    @Test
    fun `assert that all local data is loaded`() = runTest {
        val recordsList = allRecordsViewModel.allRecordsList
        assertThat(TestData.combineLocalList.containsAll(recordsList.getOrAwaitValue()), `is`(true))
        assertEquals(recordsList.getOrAwaitValue().size, TestData.combineLocalList.size)
    }

    @Test
    fun `assert that income sum is correct`() = runTest {
        val sum = allRecordsViewModel.incomeSum
        assertThat(sum.getOrAwaitValue(), `is`(TestData.localIncomeList.sumOf { it.amountReceived }))
    }
}