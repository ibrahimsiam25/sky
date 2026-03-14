package com.siam.sky.core

sealed class ResponseState<out T> {
    object Loading : ResponseState<Nothing>()
    object Idle : ResponseState<Nothing>()
    data class Success<T>(val data: T) : ResponseState<T>()
    data class Error(val message: String) : ResponseState<Nothing>()
}
