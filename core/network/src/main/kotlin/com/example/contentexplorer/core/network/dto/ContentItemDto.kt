package com.example.contentexplorer.core.network.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

/**
 * Polymorphic network DTO hierarchy representing content items.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed interface ContentItemDto {
    val id: Long
}

@Serializable
@SerialName("section")
data class SectionDto(
    override val id: Long,
    val title: String,
    val items: List<ContentItemDto>,
) : ContentItemDto

@Serializable
@SerialName("text")
data class TextDto(
    override val id: Long,
    val content: String,
) : ContentItemDto

@Serializable
@SerialName("image")
data class ImageDto(
    override val id: Long,
    val src: String,
    val title: String,
) : ContentItemDto

@Serializable
@SerialName("choice")
data class ChoiceDto(
    override val id: Long,
    val content: String,
    @SerialName("response_set") val responseSet: ResponseSetDto,
) : ContentItemDto
