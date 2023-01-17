package com.chrisojukwu.tallybookkeeping.ui.bookkeeping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetExpenseListUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetIncomeListUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetLocalExpenseListUseCase
import com.chrisojukwu.tallybookkeeping.domain.usecase.GetLocalIncomeListUseCase
import com.chrisojukwu.tallybookkeeping.testShared.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        homeViewModel =
            HomeViewModel(
                GetIncomeListUseCase(FakeRecordsRepository()),
                GetExpenseListUseCase(FakeRecordsRepository()),
                GetLocalIncomeListUseCase(FakeRecordsRepository()),
                GetLocalExpenseListUseCase(FakeRecordsRepository()),
                FakePreferencesStorage()
            )
    }

    @Test
    fun `assert that all remote data is loaded`() = runTest {
        val transactionList = homeViewModel.transactionList
        assertThat(TestData.combineList.containsAll(transactionList.getOrAwaitValue()), `is`(true))
        assertEquals(transactionList.getOrAwaitValue().size, TestData.combineList.size)
    }

    @Test
    fun `assert that local data is fetched when remote data returns error`() = runTest {
        val transactionList = homeViewModel.transactionList
        assertTrue(TestData.combineLocalList.containsAll(transactionList.getOrAwaitValue()))
        assertEquals(transactionList.getOrAwaitValue().size, TestData.combineLocalList.size)
    }

    @Test
    fun getBusinessName() {
        val expectedName = homeViewModel.businessName
        assertThat(expectedName.getOrAwaitValue(), `is`(TestData.businessName))
    }

    @Test
    fun getRecordUsingId() {
        val record = homeViewModel.getRecordUsingId("b1")
        assertThat(record, `is`(TestData.combineList.find { it.recordId == "b1" }))
    }


    @Test
    fun `assert that all-time income sum is correct`() = runTest {
        val sum = homeViewModel.allTimeIncome
        assertThat(sum.getOrAwaitValue(), `is`(TestData.incomeList.sumOf { it.amountReceived }))
    }

    @Test
    fun `assert that all-time expense sum is correct`() {
        val sum = homeViewModel.allTimeExpense
        assertThat(sum.getOrAwaitValue(), `is`(TestData.expenseList.sumOf { it.amountPaid }))
    }

}