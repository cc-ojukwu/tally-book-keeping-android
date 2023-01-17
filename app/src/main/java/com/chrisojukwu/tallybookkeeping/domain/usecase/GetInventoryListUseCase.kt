package com.chrisojukwu.tallybookkeeping.domain.usecase

import com.chrisojukwu.tallybookkeeping.domain.model.InventoryItem
import com.chrisojukwu.tallybookkeeping.domain.repository.RecordsRepository
import kotlinx.coroutines.flow.Flow
import com.chrisojukwu.tallybookkeeping.utils.Result
import javax.inject.Inject

class GetInventoryListUseCase @Inject constructor(private val repository: RecordsRepository) {

    operator fun invoke(): Flow<Result<List<InventoryItem>>> {
        return repository.getRemoteInventoryList()
    }
}