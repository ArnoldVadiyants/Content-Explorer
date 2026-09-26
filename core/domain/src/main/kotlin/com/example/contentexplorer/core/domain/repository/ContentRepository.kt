package com.example.contentexplorer.core.domain.repository

import com.example.contentexplorer.core.domain.model.ImageQuestion
import com.example.contentexplorer.core.domain.model.Page
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    fun observePages(): Flow<List<Page>>
    suspend fun refresh(): Result<Unit>
    suspend fun getImage(imageId: Long): ImageQuestion?
}
