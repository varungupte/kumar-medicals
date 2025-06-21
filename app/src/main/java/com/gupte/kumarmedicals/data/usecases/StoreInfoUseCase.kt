package com.gupte.kumarmedicals.data.usecases

import com.gupte.kumarmedicals.data.model.StoreInfo
import com.gupte.kumarmedicals.data.repositories.MedicalRepository
import com.gupte.kumarmedicals.data.model.ResponseState as ApiResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.gupte.kumarmedicals.ui.UIState

class StoreInfoUseCase @Inject constructor(
    private val repository: MedicalRepository
) {
    fun getStoreInfoResult(): Flow<UIState<StoreInfo>> = flow {
        when (val result = repository.getStoreInfo()) {
            is ApiResponseState.Success -> emit(UIState.Success(result.data))
            is ApiResponseState.Error -> emit(UIState.Failure(result.errorMessage))
        }
    }
} 