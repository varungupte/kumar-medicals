package com.gupte.kumarmedicals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MedicalItem(
    val name: String,
    val price: Double,
    val size: String,
    var quantity: Int = 0
) 