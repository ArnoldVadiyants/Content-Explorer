package com.example.contentexplorer.core.data.mapper

import com.example.contentexplorer.core.database.entity.ContentItemEntity
import com.example.contentexplorer.core.database.entity.PageEntity
import com.example.contentexplorer.core.database.entity.ResponseEntity
import com.example.contentexplorer.core.database.entity.ResponseSetEntity
import com.example.contentexplorer.core.domain.model.ChoiceQuestion
import com.example.contentexplorer.core.domain.model.Section
import com.example.contentexplorer.core.domain.model.TextQuestion
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ContentEntityToDomainMapperTest {

    @Test
    fun `mapPage rebuilds nested sections and choice responses in supplied order`() {
        val page = PageEntity(id = 1, title = "Page")
        val items = listOf(
            item(id = 2, type = "section", title = "Section", parentId = null),
            item(id = 3, type = "text", content = "Nested text", parentId = 2),
            item(id = 4, type = "choice", content = "Pick", parentId = 2),
        )
        val responseSet = ResponseSetEntity(id = 5, questionId = 4, multipleSelection = false)

        val result = ContentEntityToDomainMapper.mapPage(
            page = page,
            items = items,
            responseSets = listOf(
                ContentEntityToDomainMapper.ResponseSetData(
                    questionId = 4,
                    entity = responseSet,
                    responses = listOf(
                        ResponseEntity(id = 6, responseSetId = 5, label = "Android", score = 1),
                    ),
                ),
            ),
        )

        assertTrue(result.items.single() is Section)
        val section = result.items.single() as Section
        assertEquals("Section", section.title)
        assertTrue(section.items[0] is TextQuestion)
        assertTrue(section.items[1] is ChoiceQuestion)
        val choice = section.items[1] as ChoiceQuestion
        assertEquals("Android", choice.responseSet.responses.single().label)
        assertEquals(1, choice.responseSet.responses.single().score)
    }

    private fun item(
        id: Long,
        type: String,
        title: String? = null,
        content: String? = null,
        parentId: Long?,
    ) = ContentItemEntity(
        id = id,
        type = type,
        title = title,
        content = content,
        imageUrl = null,
        parentId = parentId,
        pageId = 1,
    )
}
