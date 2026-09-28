package com.example.contentexplorer.core.data.mapper

import com.example.contentexplorer.core.network.dto.ChoiceDto
import com.example.contentexplorer.core.network.dto.ImageDto
import com.example.contentexplorer.core.network.dto.PageDto
import com.example.contentexplorer.core.network.dto.ResponseDto
import com.example.contentexplorer.core.network.dto.ResponseSetDto
import com.example.contentexplorer.core.network.dto.SectionDto
import com.example.contentexplorer.core.network.dto.TextDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class ContentDtoToEntityMapperTest {

    @Test
    fun `map flattens nested content and choice responses`() {
        val result = ContentDtoToEntityMapper.map(
            listOf(
                PageDto(
                    id = 1,
                    type = "page",
                    title = "Page",
                    items = listOf(
                        SectionDto(
                            id = 2,
                            title = "Section",
                            items = listOf(
                                TextDto(id = 3, content = "Text"),
                                ImageDto(id = 4, src = "image.png", title = "Image"),
                                ChoiceDto(
                                    id = 5,
                                    content = "Choose",
                                    responseSet = ResponseSetDto(
                                        id = 6,
                                        multipleSelection = true,
                                        responses = listOf(ResponseDto(id = 7, label = "First", score = null)),
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        )

        assertEquals(listOf(1L), result.pages.map { it.id })
        assertEquals(listOf(2L, 3L, 4L, 5L), result.items.map { it.id })
        assertNull(result.items.first { it.id == 2L }.parentId)
        assertEquals(2L, result.items.first { it.id == 3L }.parentId)
        assertEquals("image.png", result.items.first { it.id == 4L }.imageUrl)
        assertEquals(6L, result.responseSets.single().id)
        assertEquals(5L, result.responseSets.single().questionId)
        assertEquals(7L, result.responses.single().id)
        assertNull(result.responses.single().score)
    }
}
