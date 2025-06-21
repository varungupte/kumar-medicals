package com.gupte.kumarmedicals.api

import com.gupte.kumarmedicals.data.model.MedicalItem
import com.gupte.kumarmedicals.data.model.StoreInfo
import com.gupte.kumarmedicals.util.AppConstants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMedicalApiService {
    @GET("macros/s/{scriptId}/exec")
    suspend fun getMedicalItems(
        @Path("scriptId") scriptId: String = AppConstants.GOOGLE_SCRIPT_ID,
        @Query("sheet") sheet: String = "Items"
    ): List<MedicalItem>

    @GET("macros/s/{scriptId}/exec")
    suspend fun getStoreInfo(
        @Path("scriptId") scriptId: String = AppConstants.GOOGLE_SCRIPT_ID,
        @Query("sheet") sheet: String = "StoreInfo"
    ): StoreInfo
} 