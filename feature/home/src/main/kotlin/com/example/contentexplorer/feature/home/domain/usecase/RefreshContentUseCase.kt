package com.example.contentexplorer.feature.home.domain.usecase

import com.example.contentexplorer.core.domain.repository.ContentRepository
import org.koin.core.annotation.Factory

@Factory
class RefreshContentUseCase(
    private val contentRepository: ContentRepository,
) {
    suspend operator fun invoke(): Result<Unit> = contentRepository.refresh()
}
