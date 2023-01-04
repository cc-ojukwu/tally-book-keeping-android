package com.chrisojukwu.tallybookkeeping.domain.usecase

import com.chrisojukwu.tallybookkeeping.domain.model.StockItem
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocalInventoryListUseCase @Inject constructor (private val repository: RecordsRepository) {

    operator fun invoke(): Flow<List<StockItem>> {
        return repository.getAllLocalInventory()
    }
}