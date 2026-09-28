package com.example.contentexplorer.core.data.repository

import com.example.contentexplorer.core.data.mapper.ContentDtoToEntityMapper
import com.example.contentexplorer.core.data.mapper.ContentEntityToDomainMapper
import com.example.contentexplorer.core.database.dao.ContentDao
import com.example.contentexplorer.core.domain.model.ImageQuestion
import com.example.contentexplorer.core.domain.model.Page
import com.example.contentexplorer.core.domain.repository.ContentRepository
import com.example.contentexplorer.core.network.api.ContentApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Singleton

/**
 * Repository implementation coordinating local database storage and remote API syncing.
 */
@Singleton
class ContentRepositoryImpl(
    private val contentApi: ContentApi,
    private val contentDao: ContentDao,
) : ContentRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observePages(): Flow<List<Page>> =
        contentDao.observePages().mapLatest { pageEntities ->
            // Reconstruct nested domain model for each stored page
            pageEntities.map { pageEntity ->
                val items = contentDao.getItemsForPage(pageEntity.id)
                val choiceItems = items.filter { it.type == "choice" }
                val responseSets = choiceItems.mapNotNull { item ->
                    contentDao.getResponseSet(item.id)?.let { rs ->
                        val responses = contentDao.getResponses(rs.id)
                        ContentEntityToDomainMapper.ResponseSetData(item.id, rs, responses)
                    }
                }
                ContentEntityToDomainMapper.mapPage(pageEntity, items, responseSets)
            }
        }

    override suspend fun refresh(): Result<Unit> = runCatching {
        val pages = contentApi.fetchPages()
        val contentEntity = ContentDtoToEntityMapper.map(pages)
        // Atomically overwrite local database cache with newly fetched pages
        contentDao.replaceAll(contentEntity.pages, contentEntity.items, contentEntity.responseSets, contentEntity.responses)
    }

    override suspend fun getImage(imageId: Long): ImageQuestion? {
        val entity = contentDao.getItemById(imageId) ?: return null
        if (entity.type != "image") return null
        return ImageQuestion(id = entity.id, src = entity.imageUrl.orEmpty(), title = entity.title.orEmpty())
    }
}
