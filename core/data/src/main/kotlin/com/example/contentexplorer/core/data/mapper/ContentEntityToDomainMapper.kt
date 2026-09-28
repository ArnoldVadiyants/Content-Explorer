package com.example.contentexplorer.core.data.mapper

import com.example.contentexplorer.core.database.entity.ContentItemEntity
import com.example.contentexplorer.core.database.entity.PageEntity
import com.example.contentexplorer.core.database.entity.ResponseEntity
import com.example.contentexplorer.core.database.entity.ResponseSetEntity
import com.example.contentexplorer.core.domain.model.ChoiceQuestion
import com.example.contentexplorer.core.domain.model.ContentItem
import com.example.contentexplorer.core.domain.model.ImageQuestion
import com.example.contentexplorer.core.domain.model.Page
import com.example.contentexplorer.core.domain.model.Response
import com.example.contentexplorer.core.domain.model.ResponseSet
import com.example.contentexplorer.core.domain.model.Section
import com.example.contentexplorer.core.domain.model.TextQuestion

/**
 * Maps database entities into domain models.
 */
object ContentEntityToDomainMapper {

    data class ResponseSetData(
        val questionId: Long,
        val entity: ResponseSetEntity,
        val responses: List<ResponseEntity>,
    )

    /**
     * Reconstructs a nested domain [Page] from flat database entities.
     */
    fun mapPage(
        page: PageEntity,
        items: List<ContentItemEntity>,
        responseSets: List<ResponseSetData>,
    ): Page {
        val responseSetsByQuestionId = responseSets.associateBy { it.questionId }
        // Filter top-level items (items without a parent section)
        val topLevelItems = items.filter { it.parentId == null }
        return Page(
            id = page.id,
            title = page.title,
            items = topLevelItems.map { mapItem(it, items, responseSetsByQuestionId) },
        )
    }

    // Recursively constructs section children and choice question models
    private fun mapItem(
        item: ContentItemEntity,
        allItems: List<ContentItemEntity>,
        responseSetsByQuestionId: Map<Long, ResponseSetData>,
    ): ContentItem = when (item.type) {
        "section" -> {
            val children = allItems.filter { it.parentId == item.id }
            Section(
                id = item.id,
                title = item.title.orEmpty(),
                items = children.map { mapItem(it, allItems, responseSetsByQuestionId) },
            )
        }
        "text" -> TextQuestion(id = item.id, content = item.content.orEmpty())
        "image" -> ImageQuestion(id = item.id, src = item.imageUrl.orEmpty(), title = item.title.orEmpty())
        "choice" -> {
            val rsData = responseSetsByQuestionId[item.id]
            val responseSet = rsData?.let { data ->
                ResponseSet(
                    id = data.entity.id,
                    multipleSelection = data.entity.multipleSelection,
                    responses = data.responses.map { r ->
                        Response(id = r.id, label = r.label, score = r.score)
                    },
                )
            } ?: ResponseSet(id = -1, multipleSelection = false, responses = emptyList())
            ChoiceQuestion(id = item.id, content = item.content.orEmpty(), responseSet = responseSet)
        }
        else -> TextQuestion(id = item.id, content = item.content.orEmpty())
    }
}
