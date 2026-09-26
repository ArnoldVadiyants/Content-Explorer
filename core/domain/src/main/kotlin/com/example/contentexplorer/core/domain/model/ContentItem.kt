package com.example.contentexplorer.core.domain.model

sealed interface ContentItem {
    val id: Long
}

data class Section(
    override val id: Long,
    val title: String,
    val items: List<ContentItem>,
) : ContentItem

data class TextQuestion(
    override val id: Long,
    val content: String,
) : ContentItem

data class ImageQuestion(
    override val id: Long,
    val src: String,
    val title: String,
) : ContentItem

data class ChoiceQuestion(
    override val id: Long,
    val content: String,
    val responseSet: ResponseSet,
) : ContentItem
