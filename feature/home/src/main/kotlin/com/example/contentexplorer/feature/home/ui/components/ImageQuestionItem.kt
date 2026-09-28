package com.example.contentexplorer.feature.home.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.contentexplorer.core.designsystem.theme.Spacing
import com.example.contentexplorer.core.domain.model.ImageQuestion
import com.example.contentexplorer.feature.home.presentation.HomeAction

@Composable
fun ImageQuestionItem(
    question: ImageQuestion,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.md),
    ) {
        AsyncImage(
            model = question.src,
            contentDescription = question.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("image_question_image")
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(12.dp))
                .clickable { onAction(HomeAction.ImageClicked(question.id)) },
        )
        Spacer(modifier = Modifier.height(Spacing.xs))
        Text(
            text = question.title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
