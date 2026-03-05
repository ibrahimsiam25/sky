package com.siam.sky.core

sealed class ApiState<out T> {
    object Loading : ApiState<Nothing>()
    object Idle : ApiState<Nothing>()
    data class Success<T>(val data: T) : ApiState<T>()
    data class Error(val message: String) : ApiState<Nothing>()
}
