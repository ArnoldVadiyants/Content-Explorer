package com.example.contentexplorer.feature.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.contentexplorer.core.designsystem.theme.Spacing
import com.example.contentexplorer.core.domain.model.Page
import com.example.contentexplorer.feature.home.presentation.HomeAction

@Composable
fun PageContent(
    page: Page,
    selectedResponses: Map<Long, Set<Long>>,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = Spacing.sm, bottom = Spacing.md),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
        page.items.forEach { item ->
            ContentItemContent(
                item = item,
                depth = 0,
                selectedResponses = selectedResponses,
                onAction = onAction,
            )
        }
    }
}
