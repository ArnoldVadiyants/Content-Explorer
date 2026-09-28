package com.example.contentexplorer.feature.imagedetail.domain.usecase

import com.example.contentexplorer.core.domain.model.ImageQuestion
import com.example.contentexplorer.core.domain.repository.ContentRepository
import org.koin.core.annotation.Factory

@Factory
class GetImageDetailUseCase(
    private val contentRepository: ContentRepository,
) {
    suspend operator fun invoke(imageId: Long): ImageQuestion? = contentRepository.getImage(imageId)
}
