package com.example.contentexplorer.feature.home.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.contentexplorer.core.designsystem.theme.Spacing
import com.example.contentexplorer.core.domain.model.Section
import com.example.contentexplorer.feature.home.presentation.HomeAction

/**
 * Renders a section header and recursively renders its nested children with indentation.
 */
@Composable
fun SectionContent(
    section: Section,
    depth: Int,
    selectedResponses: Map<Long, Set<Long>>,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Calculates visual indent and typography scaling based on nesting level
    val indent = (Spacing.md * depth).coerceAtMost(Spacing.xl)
    val style = if (depth == 0) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium
    val weight = if (depth == 0) FontWeight.SemiBold else FontWeight.Medium

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = indent, bottom = Spacing.md),
    ) {
        Text(
            text = section.title,
            style = style,
            fontWeight = weight,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(Spacing.sm))
        section.items.forEach { child ->
            ContentItemContent(
                item = child,
                depth = depth + 1,
                selectedResponses = selectedResponses,
                onAction = onAction,
            )
        }
    }
}
