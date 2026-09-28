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
import com.example.contentexplorer.core.domain.model.ChoiceQuestion
import com.example.contentexplorer.feature.home.presentation.HomeAction

@Composable
fun ChoiceQuestionItem(
    question: ChoiceQuestion,
    selectedResponseIds: Set<Long>,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = Spacing.md),
    ) {
        Text(
            text = question.content,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(Spacing.xs))
        question.responseSet.responses.forEach { response ->
            ChoiceResponseRow(
                response = response,
                selected = response.id in selectedResponseIds,
                multipleSelection = question.responseSet.multipleSelection,
                onClick = { onAction(HomeAction.ResponseSelected(question.id, response.id)) },
            )
        }
    }
}
