// Sealed interface representing different states of the UI (Loading, Success, Error, Idle)
package com.gupte.kumarmedicals.ui

sealed interface UIState<out T> {
    data object Loading : UIState<Nothing>
    data object Idle : UIState<Nothing>
    data class Success<T>(val data: T) : UIState<T>
    data class Failure<T>(val message: String) : UIState<T>
} 