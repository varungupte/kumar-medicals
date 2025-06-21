package com.gupte.kumarmedicals.data.repositories

import com.gupte.kumarmedicals.api.IMedicalApiService
import com.gupte.kumarmedicals.data.model.MedicalItem
import com.gupte.kumarmedicals.data.model.ResponseState
import com.gupte.kumarmedicals.data.model.StoreInfo
import com.gupte.kumarmedicals.util.AppConstants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicalRepository @Inject constructor(
    private val apiService: IMedicalApiService
) {
    suspend fun getMedicalItems(): ResponseState<List<MedicalItem>> {
        return try {
            val items = apiService.getMedicalItems()
            if (items.isNotEmpty()) {
                ResponseState.Success(items)
            } else {
                ResponseState.Error(AppConstants.GENERIC_ERROR)
            }
        } catch (e: Exception) {
            ResponseState.Error(AppConstants.GENERIC_ERROR)
        }
    }

    suspend fun getStoreInfo(): ResponseState<StoreInfo> {
        return try {
            val storeInfo = apiService.getStoreInfo()
            ResponseState.Success(storeInfo)
        } catch (e: Exception) {
            ResponseState.Error(AppConstants.GENERIC_ERROR)
        }
    }
} 