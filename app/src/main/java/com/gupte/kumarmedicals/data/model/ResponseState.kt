package com.gupte.kumarmedicals.data.model

// Represents the API or repository response state
sealed interface ResponseState<out T> {
    data class Success<T>(val data: T) : ResponseState<T>
    data class Error<T>(val errorMessage: String) : ResponseState<T>
} 