package com.example.contentexplorer.feature.imagedetail.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import coil3.compose.AsyncImage
import com.example.contentexplorer.core.designsystem.theme.Spacing
import com.example.contentexplorer.core.domain.model.ImageQuestion

@Composable
fun ImageDetailContent(
    image: ImageQuestion,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier
        .fillMaxSize()
        .testTag("image_detail_content")) {
        item(key = "image") {
            AsyncImage(
                model = image.src,
                contentDescription = image.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
        item(key = "info") {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing.lg),
                text = image.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
