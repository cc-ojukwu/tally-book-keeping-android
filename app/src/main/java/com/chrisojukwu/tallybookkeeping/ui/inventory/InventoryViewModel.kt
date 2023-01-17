package com.chrisojukwu.tallybookkeeping.ui.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.chrisojukwu.tallybookkeeping.domain.model.InventoryItem
import com.chrisojukwu.tallybookkeeping.domain.model.StringResponse
import com.chrisojukwu.tallybookkeeping.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val getInventoryListUseCase: GetInventoryListUseCase,
    private val getLocalInventoryListUseCase: GetLocalInventoryListUseCase,
    private val deleteInventoryUseCase: DeleteInventoryUseCase,
    private val updateInventoryItemUseCase: UpdateInventoryItemUseCase,
    private val saveInventoryItemUseCase: SaveInventoryItemUseCase
) : ViewModel() {

    private val _inventoryList = MutableLiveData<List<InventoryItem>>(listOf())
    val inventoryList: LiveData<List<InventoryItem>> = _inventoryList

    private val _totalStockValue = Transformations.map(_inventoryList) { list ->
        list.sumOf {
            it.costPrice.multiply(it.quantity.toBigDecimal())
        }
    }
    val totalStockValue: LiveData<BigDecimal> = _totalStockValue

    private val _stockName = MutableLiveData<String>()
    val stockName: LiveData<String> = _stockName

    private val _costPrice = MutableLiveData(BigDecimal.ZERO)
    val costPrice: LiveData<BigDecimal> = _costPrice

    private val _sellingPrice = MutableLiveData(BigDecimal.ZERO)
    val sellingPrice: LiveData<BigDecimal> = _sellingPrice

    private val _quantityLeft = MutableLiveData(1)
    val quantityLeft: LiveData<Int> = _quantityLeft

    private val _editInventoryItem = MutableLiveData<InventoryItem>()
    val editInventoryItem: LiveData<InventoryItem> = _editInventoryItem

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _inventoryItem = MutableLiveData<InventoryItem>()

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    init {
        getInventoryData()
    }

    fun getInventoryData() {
        getInventoryListUseCase()
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        result.data.let {
                            _inventoryList.value = it
                        }
                    }
                    is Result.Error -> {
                        getLocalData()
                    }
                    is Result.Loading -> {}
                }
            }.launchIn(viewModelScope)
    }

    private fun getLocalData() {
        getLocalInventoryListUseCase()
            .onEach {
                _inventoryList.value = it
            }.launchIn(viewModelScope)
    }

    fun setStockDetails(inventoryItem: InventoryItem) {
        _stockName.value = inventoryItem.stockName
        _inventoryItem.value = inventoryItem
        _costPrice.value = inventoryItem.costPrice
        _sellingPrice.value = inventoryItem.sellingPrice
        _quantityLeft.value = inventoryItem.quantity
    }

    fun setEditStockDetails(inventoryItem: InventoryItem) {
        _editInventoryItem.value = inventoryItem
    }

    fun saveNewStockItem(inventoryItem: InventoryItem): Flow<Result<StringResponse>> =
        saveInventoryItemUseCase(inventoryItem)

    fun updateStockItem(inventoryItem: InventoryItem) {
        _inventoryItem.value = inventoryItem
    }

    fun deleteInventory(): Flow<Result<StringResponse>> =
        deleteInventoryUseCase(_inventoryItem.value!!)

    fun updateInventoryItem(): Flow<Result<StringResponse>> =
        updateInventoryItemUseCase(_inventoryItem.value!!)



}