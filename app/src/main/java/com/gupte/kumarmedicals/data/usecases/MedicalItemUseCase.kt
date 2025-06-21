// Business logic layer that transforms repository data into UI state
package com.gupte.kumarmedicals.data.usecases

import com.gupte.kumarmedicals.data.model.MedicalItem
import com.gupte.kumarmedicals.data.repositories.MedicalRepository
import com.gupte.kumarmedicals.data.model.ResponseState as ApiResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.gupte.kumarmedicals.ui.UIState

class MedicalItemUseCase @Inject constructor(
    private val repository: MedicalRepository
) {
    fun getMedicalItemResult(): Flow<UIState<List<MedicalItem>>> = flow {
        when (val result = repository.getMedicalItems()) {
            is ApiResponseState.Success -> emit(UIState.Success(result.data))
            is ApiResponseState.Error -> emit(UIState.Failure(result.errorMessage))
        }
    }
}