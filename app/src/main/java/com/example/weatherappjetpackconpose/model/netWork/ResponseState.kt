package com.example.weatherappjetpackconpose.model.netWork

sealed class ResponseState<out T> {
    data class Success<out T>(val data: T) : ResponseState<T>()
    data class Error(val message: Throwable) : ResponseState<Nothing>()
    object Loading : ResponseState<Nothing>()
}