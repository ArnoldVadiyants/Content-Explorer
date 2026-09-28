package com.example.contentexplorer.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDto(
    val id: Long,
    val label: String,
    val score: Int? = null,
)
