package com.gupte.kumarmedicals.api

import com.gupte.kumarmedicals.data.model.SheetsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface IMedicalApiService {
    @GET("spreadsheets/d/{spreadsheetId}/export?format=csv&gid=0")
    suspend fun getSheetData(
        @Path("spreadsheetId") spreadsheetId: String
    ): String
    
    @GET("spreadsheets/d/{spreadsheetId}/export?format=csv&gid=0")
    suspend fun getStoreInfo(
        @Path("spreadsheetId") spreadsheetId: String
    ): String
} 