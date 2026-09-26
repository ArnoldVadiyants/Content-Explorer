package com.example.contentexplorer.core.network.error

sealed class NetworkError : Exception() {
    data object Network : NetworkError()
    data class Http(val code: Int) : NetworkError()
    data object Serialization : NetworkError()
    data object Unknown : NetworkError()
}
