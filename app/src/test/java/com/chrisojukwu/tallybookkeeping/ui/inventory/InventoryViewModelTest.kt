package com.chrisojukwu.tallybookkeeping.ui.inventory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chrisojukwu.tallybookkeeping.domain.usecase.*
import com.chrisojukwu.tallybookkeeping.testShared.FakeRecordsRepository
import com.chrisojukwu.tallybookkeeping.testShared.MainCoroutineRule
import com.chrisojukwu.tallybookkeeping.testShared.TestData
import com.chrisojukwu.tallybookkeeping.testShared.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InventoryViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var inventoryViewModel: InventoryViewModel

    @Before
    fun setUp() {
        inventoryViewModel =
            InventoryViewModel(
                GetInventoryListUseCase(FakeRecordsRepository()),
                GetLocalInventoryListUseCase(FakeRecordsRepository()),
                DeleteInventoryUseCase(FakeRecordsRepository()),
                UpdateInventoryItemUseCase(FakeRecordsRepository()),
                SaveInventoryItemUseCase(FakeRecordsRepository())
            )
    }

    @Test
    fun `getInventoryData`() = runTest {

        val inventoryList = inventoryViewModel.inventoryList

        assertThat(inventoryList.getOrAwaitValue(), `is`(TestData.stockList))
    }
}