package com.chrisojukwu.tallybookkeeping.ui.inventory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chrisojukwu.tallybookkeeping.utils.Result
import com.chrisojukwu.tallybookkeeping.domain.model.StockItem
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
    private val updateInventoryUseCase: UpdateInventoryUseCase,
    private val saveInventoryUseCase: SaveInventoryUseCase
) : ViewModel() {

    private val _inventoryList = MutableLiveData<List<StockItem>>(listOf())
    val inventoryList: LiveData<List<StockItem>> = _inventoryList

    private val _totalStockValue = Transformations.map(_inventoryList) { list ->
        list.sumOf { it.costPrice  }
    }
    val totalStockValue: LiveData<BigDecimal> = _totalStockValue

    private val _costPrice = MutableLiveData(BigDecimal.ZERO)
    val costPrice: LiveData<BigDecimal> = _costPrice

    private val _sellingPrice = MutableLiveData(BigDecimal.ZERO)
    val sellingPrice: LiveData<BigDecimal> = _sellingPrice

    private val _quantityLeft = MutableLiveData(1)
    val quantityLeft: LiveData<Int> = _quantityLeft

    private val _editStockItem = MutableLiveData<StockItem>()
    val editStockItem: LiveData<StockItem> = _editStockItem

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _stockItem = MutableLiveData<StockItem>()

    private val _isLocalData = MutableLiveData(false)
    val isLocalData: LiveData<Boolean> = _isLocalData

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    init {
        getInventoryData()
    }

    private fun getInventoryData() {
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
                _isLocalData.postValue(true)
                _inventoryList.value = it
            }.launchIn(viewModelScope)
    }

    fun setStockDetails(stockItem: StockItem) {
        _stockItem.value = stockItem
        _costPrice.value = stockItem.costPrice
        _sellingPrice.value = stockItem.sellingPrice
        _quantityLeft.value = stockItem.quantity
    }

    fun setEditStockDetails(stockItem: StockItem) {
        _editStockItem.value = stockItem
    }

    fun saveNewStockItem(stockItem: StockItem): Flow<Result<String>> =
        saveInventoryUseCase(stockItem)

    fun updateStockItem(stockItem: StockItem) {
        _stockItem.value = stockItem
    }

    fun deleteInventory(): Flow<Result<String>> =
        deleteInventoryUseCase(_stockItem.value!!)

    fun updateInventoryItem(): Flow<Result<String>> =
        updateInventoryUseCase(_stockItem.value!!)



}