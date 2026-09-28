package com.example.contentexplorer.feature.imagedetail.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data class ImageDetail(
    val imageId: Long,
) : NavKey
