package com.example.contentexplorer.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class PageDto(
    val id: Long,
    val title: String,
    val items: List<ContentItemDto>,
)
