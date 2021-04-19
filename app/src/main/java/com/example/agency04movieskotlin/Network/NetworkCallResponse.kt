package com.example.agency04movieskotlin.Network

sealed class NetworkCallResponse<out R> {
    data class Success<out T>(val data: T) : NetworkCallResponse<T>()
    data class Error(val exception: Exception) : NetworkCallResponse<Nothing>()
}
