package com.chrisojukwu.tallybookkeeping.domain.usecase

import com.chrisojukwu.tallybookkeeping.domain.model.StockItem
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import com.chrisojukwu.tallybookkeeping.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteInventoryUseCase @Inject constructor(private val repository: RecordsRepository) {

    operator fun invoke(stockItem: StockItem): Flow<Result<String>> =
         repository.deleteInventory(stockItem)

}