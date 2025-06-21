package com.gupte.kumarmedicals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SheetsResponse(
    val values: List<List<String>>
) 