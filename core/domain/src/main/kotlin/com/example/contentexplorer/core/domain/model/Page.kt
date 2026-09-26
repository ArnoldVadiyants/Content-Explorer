package com.example.contentexplorer.core.domain.model

data class Page(
    val id: Long,
    val title: String,
    val items: List<ContentItem>,
)
