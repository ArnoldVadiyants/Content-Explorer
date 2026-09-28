package com.example.contentexplorer.feature.home.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.contentexplorer.core.designsystem.theme.Spacing
import com.example.contentexplorer.core.domain.model.TextQuestion

@Composable
fun TextQuestionItem(
    question: TextQuestion,
    modifier: Modifier = Modifier,
) {
    Text(
        text = question.content,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.md),
    )
}
