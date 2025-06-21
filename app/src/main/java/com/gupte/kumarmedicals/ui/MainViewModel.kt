// Manages search state, user input, and coordinates data flow between UI and repository
package com.gupte.kumarmedicals.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gupte.kumarmedicals.data.usecases.MedicalItemUseCase
import com.gupte.kumarmedicals.data.usecases.StoreInfoUseCase
import com.gupte.kumarmedicals.data.model.MedicalItem
import com.gupte.kumarmedicals.data.model.StoreInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val medicalItemUseCase: MedicalItemUseCase,
    private val storeInfoUseCase: StoreInfoUseCase
) : ViewModel() {

    private val _searchState = MutableLiveData<UIState<List<MedicalItem>>>(UIState.Idle)
    val searchState: LiveData<UIState<List<MedicalItem>>> = _searchState

    private val _items = MutableStateFlow<List<MedicalItem>>(emptyList())
    val items: StateFlow<List<MedicalItem>> = _items

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _storeInfo = MutableStateFlow<StoreInfo?>(null)
    val storeInfo: StateFlow<StoreInfo?> = _storeInfo

    init {
        fetchMedicalItems()
        fetchStoreInfo()
    }

    private fun fetchMedicalItems() {
        _searchState.value = UIState.Loading
        viewModelScope.launch {
            medicalItemUseCase.getMedicalItemResult()
                .flowOn(Dispatchers.IO)
                .catch { error -> 
                    _searchState.value = UIState.Failure(error.message.orEmpty()) 
                }
                .collect { state ->
                    _searchState.value = state
                    when (state) {
                        is UIState.Success -> {
                            _items.value = state.data
                        }
                        is UIState.Failure -> {
                            // Error is already handled in searchState
                        }
                        is UIState.Loading -> {
                            // Loading is already handled in searchState
                        }
                        is UIState.Idle -> {
                            // Idle state
                        }
                    }
                }
        }
    }

    private fun fetchStoreInfo() {
        viewModelScope.launch {
            storeInfoUseCase.getStoreInfoResult()
                .flowOn(Dispatchers.IO)
                .catch { error -> 
                    // Handle store info error silently for now
                }
                .collect { state ->
                    when (state) {
                        is UIState.Success -> {
                            _storeInfo.value = state.data
                        }
                        is UIState.Failure -> {
                            // Handle store info error silently for now
                        }
                        is UIState.Loading -> {
                            // Loading state
                        }
                        is UIState.Idle -> {
                            // Idle state
                        }
                    }
                }
        }
    }

    fun updateQuantity(itemName: String, quantity: Int) {
        _items.value = _items.value.map { item ->
            if (item.name == itemName) {
                item.copy(quantity = quantity)
            } else item
        }
    }

    fun getOrderItems(): List<MedicalItem> {
        return _items.value.filter { it.quantity > 0 }
    }

    fun getTotalAmount(): Double {
        return getOrderItems().sumOf { it.price * it.quantity }
    }

    fun getStoreEmail(): String? {
        return _storeInfo.value?.emailId
    }

    fun getStoreName(): String? {
        return _storeInfo.value?.title
    }

    fun getStoreAddress(): String? {
        return _storeInfo.value?.address
    }

    fun getStorePhoneNumber(): String? {
        return _storeInfo.value?.phoneNumber
    }
}