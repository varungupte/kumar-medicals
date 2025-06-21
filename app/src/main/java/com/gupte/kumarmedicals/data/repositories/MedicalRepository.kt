package com.gupte.kumarmedicals.data.repositories

import com.gupte.kumarmedicals.api.IMedicalApiService
import com.gupte.kumarmedicals.data.model.MedicalItem
import com.gupte.kumarmedicals.data.model.StoreInfo
import com.gupte.kumarmedicals.data.model.ResponseState
import com.gupte.kumarmedicals.util.AppConstants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicalRepository @Inject constructor(
    private val apiService: IMedicalApiService
) {
    suspend fun getMedicalItems(): ResponseState<List<MedicalItem>> {
        return try {
            val csvData = apiService.getSheetData(
                spreadsheetId = AppConstants.MEDICAL_ITEMS_SPREADSHEET_ID
            )
            val items = parseCsvData(csvData)
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
            val csvData = apiService.getStoreInfo(
                spreadsheetId = AppConstants.STORE_INFO_SPREADSHEET_ID
            )
            val storeInfo = parseStoreInfoCsvData(csvData)
            if (storeInfo != null) {
                ResponseState.Success(storeInfo)
            } else {
                ResponseState.Error(AppConstants.GENERIC_ERROR)
            }
        } catch (e: Exception) {
            ResponseState.Error(AppConstants.GENERIC_ERROR)
        }
    }
    
    private fun parseCsvData(csvData: String): List<MedicalItem> {
        return csvData.trim().split("\n")
            .drop(1) // Skip header row
            .mapNotNull { line ->
                val columns = line.split(",")
                if (columns.size >= 3) {
                    try {
                        MedicalItem(
                            name = columns[0].trim().removeSurrounding("\""),
                            price = columns[1].trim().removeSurrounding("\"").toDoubleOrNull() ?: 0.0,
                            size = columns[2].trim().removeSurrounding("\"")
                        )
                    } catch (e: Exception) {
                        null
                    }
                } else {
                    null
                }
            }
    }
    
    private fun parseStoreInfoCsvData(csvData: String): StoreInfo? {
        return try {
            val lines = csvData.trim().split("\n")
            if (lines.size >= 2) { // At least header + one data row
                val dataLine = lines[1] // Get first data row
                
                // Parse CSV with proper handling of quoted fields
                val columns = parseCsvLine(dataLine)
                if (columns.size >= 4) {
                    StoreInfo(
                        title = columns[0].trim(),
                        emailId = columns[1].trim(),
                        address = columns[2].trim(),
                        phoneNumber = columns[3].trim()
                    )
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        var current = StringBuilder()
        var inQuotes = false
        var i = 0
        
        while (i < line.length) {
            val char = line[i]
            when {
                char == '"' -> {
                    if (inQuotes && i + 1 < line.length && line[i + 1] == '"') {
                        // Escaped quote
                        current.append('"')
                        i += 2 // Skip both quotes
                    } else {
                        // Toggle quote state
                        inQuotes = !inQuotes
                        i++
                    }
                }
                char == ',' && !inQuotes -> {
                    // End of field
                    result.add(current.toString())
                    current.clear()
                    i++
                }
                else -> {
                    current.append(char)
                    i++
                }
            }
        }
        
        // Add the last field
        result.add(current.toString())
        return result
    }
} 