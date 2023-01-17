package com.chrisojukwu.tallybookkeeping.domain.usecase

import com.chrisojukwu.tallybookkeeping.domain.model.InventoryItem
import com.chrisojukwu.tallybookkeeping.domain.model.StringResponse
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import com.chrisojukwu.tallybookkeeping.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpdateInventoryItemUseCase @Inject constructor(private val repository: RecordsRepository) {

    operator fun invoke(inventoryItem: InventoryItem): Flow<Result<StringResponse>> =
         repository.updateInventoryItem(inventoryItem)
}