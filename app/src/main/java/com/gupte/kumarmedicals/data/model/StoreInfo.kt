package com.gupte.kumarmedicals.data.model

import kotlinx.serialization.Serializable

@Serializable
data class StoreInfo(
    val title: String,
    val emailId: String,
    val address: String,
    val phoneNumber: String
) 