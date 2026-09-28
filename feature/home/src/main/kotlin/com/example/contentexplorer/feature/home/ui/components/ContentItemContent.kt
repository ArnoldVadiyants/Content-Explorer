package com.example.contentexplorer.feature.home.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.contentexplorer.core.domain.model.ChoiceQuestion
import com.example.contentexplorer.core.domain.model.ContentItem
import com.example.contentexplorer.core.domain.model.ImageQuestion
import com.example.contentexplorer.core.domain.model.Section
import com.example.contentexplorer.core.domain.model.TextQuestion
import com.example.contentexplorer.feature.home.presentation.HomeAction

/**
 * Polymorphic composable dispatcher for rendering different [ContentItem] types.
 */
@Composable
fun ContentItemContent(
    item: ContentItem,
    depth: Int,
    selectedResponses: Map<Long, Set<Long>>,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (item) {
        is Section -> SectionContent(
            section = item,
            depth = depth,
            selectedResponses = selectedResponses,
            onAction = onAction,
            modifier = modifier,
        )

        is TextQuestion -> TextQuestionItem(question = item, modifier = modifier)

        is ImageQuestion -> ImageQuestionItem(question = item, onAction = onAction, modifier = modifier)

        is ChoiceQuestion -> ChoiceQuestionItem(
            question = item,
            selectedResponseIds = selectedResponses[item.id].orEmpty(),
            onAction = onAction,
            modifier = modifier,
        )
    }
}
