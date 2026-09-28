package com.example.contentexplorer.feature.home.domain.usecase

import com.example.contentexplorer.core.domain.model.Page
import com.example.contentexplorer.core.domain.repository.ContentRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class ObserveContentUseCase(
    private val contentRepository: ContentRepository,
) {
    operator fun invoke(): Flow<List<Page>> = contentRepository.observePages()
}
