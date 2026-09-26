package com.example.contentexplorer.core.data.mapper

import com.example.contentexplorer.core.database.entity.ContentItemEntity
import com.example.contentexplorer.core.database.entity.PageEntity
import com.example.contentexplorer.core.database.entity.ResponseEntity
import com.example.contentexplorer.core.database.entity.ResponseSetEntity
import com.example.contentexplorer.core.network.dto.ChoiceDto
import com.example.contentexplorer.core.network.dto.ContentItemDto
import com.example.contentexplorer.core.network.dto.ImageDto
import com.example.contentexplorer.core.network.dto.PageDto
import com.example.contentexplorer.core.network.dto.SectionDto
import com.example.contentexplorer.core.network.dto.TextDto

object ContentDtoToEntityMapper {

    data class ContentEntity(
        val pages: List<PageEntity>,
        val items: List<ContentItemEntity>,
        val responseSets: List<ResponseSetEntity>,
        val responses: List<ResponseEntity>,
    )

    fun map(pages: List<PageDto>): ContentEntity {
        val pageEntities = mutableListOf<PageEntity>()
        val itemEntities = mutableListOf<ContentItemEntity>()
        val responseSetEntities = mutableListOf<ResponseSetEntity>()
        val responseEntities = mutableListOf<ResponseEntity>()

        pages.forEach { page ->
            pageEntities.add(PageEntity(id = page.id, title = page.title))
            page.items.forEach { item ->
                flattenItem(item, pageId = page.id, parentId = null, itemEntities, responseSetEntities, responseEntities)
            }
        }

        return ContentEntity(pageEntities, itemEntities, responseSetEntities, responseEntities)
    }

    private fun flattenItem(
        item: ContentItemDto,
        pageId: Long,
        parentId: Long?,
        items: MutableList<ContentItemEntity>,
        responseSets: MutableList<ResponseSetEntity>,
        responses: MutableList<ResponseEntity>,
    ) {
        when (item) {
            is SectionDto -> {
                items.add(
                    ContentItemEntity(
                        id = item.id,
                        type = "section",
                        title = item.title,
                        content = null,
                        imageUrl = null,
                        parentId = parentId,
                        pageId = pageId,
                    ),
                )
                item.items.forEach { child ->
                    flattenItem(child, pageId = pageId, parentId = item.id, items, responseSets, responses)
                }
            }
            is TextDto -> {
                items.add(
                    ContentItemEntity(
                        id = item.id,
                        type = "text",
                        title = null,
                        content = item.content,
                        imageUrl = null,
                        parentId = parentId,
                        pageId = pageId,
                    ),
                )
            }
            is ImageDto -> {
                items.add(
                    ContentItemEntity(
                        id = item.id,
                        type = "image",
                        title = item.title,
                        content = null,
                        imageUrl = item.src,
                        parentId = parentId,
                        pageId = pageId,
                    ),
                )
            }
            is ChoiceDto -> {
                items.add(
                    ContentItemEntity(
                        id = item.id,
                        type = "choice",
                        title = null,
                        content = item.content,
                        imageUrl = null,
                        parentId = parentId,
                        pageId = pageId,
                    ),
                )
                responseSets.add(
                    ResponseSetEntity(
                        id = item.responseSet.id,
                        questionId = item.id,
                        multipleSelection = item.responseSet.multipleSelection,
                    ),
                )
                item.responseSet.responses.forEach { r ->
                    responses.add(
                        ResponseEntity(
                            id = r.id,
                            responseSetId = item.responseSet.id,
                            label = r.label,
                            score = r.score,
                        ),
                    )
                }
            }
        }
    }
}
