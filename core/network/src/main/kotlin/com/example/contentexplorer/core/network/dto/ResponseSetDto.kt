package com.example.contentexplorer.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSetDto(
    val id: Long,
    @SerialName("multiple_selection") val multipleSelection: Boolean,
    val responses: List<ResponseDto>,
)
