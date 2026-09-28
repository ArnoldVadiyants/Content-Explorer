package com.example.contentexplorer.feature.imagedetail.ui

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.contentexplorer.core.domain.model.ImageQuestion
import com.example.contentexplorer.feature.imagedetail.R
import com.example.contentexplorer.feature.imagedetail.presentation.ImageDetailAction
import com.example.contentexplorer.feature.imagedetail.presentation.ImageDetailUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun whenLoading_displaysLoadingIndicator() {
        composeTestRule.setContent {
            ImageDetailScreen(
                state = ImageDetailUiState(
                    isLoading = true,
                    image = null,
                    error = false,
                ),
                onAction = {},
            )
        }

        composeTestRule
            .onNode(
                hasProgressBarRangeInfo(
                    ProgressBarRangeInfo.Indeterminate,
                ),
            )
            .assertIsDisplayed()
    }

    @Test
    fun whenErrorIsDisplayed_displaysErrorAndRetryDispatchesRetryAction() {
        val actions = mutableListOf<ImageDetailAction>()

        composeTestRule.setContent {
            ImageDetailScreen(
                state = ImageDetailUiState(
                    isLoading = false,
                    image = null,
                    error = true,
                ),
                onAction = actions::add,
            )
        }

        composeTestRule
            .onNodeWithText(
                targetContext.getString(R.string.image_detail_error),
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                targetContext.getString(R.string.content_retry),
            )
            .performClick()

        assertEquals(
            listOf(ImageDetailAction.Retry),
            actions,
        )
    }

    @Test
    fun whenImageIsLoaded_displaysImageAndTitle() {
        val image = ImageQuestion(
            id = 100L,
            src = "https://example.com/test_image.png",
            title = "Detailed Sample Image",
        )

        composeTestRule.setContent {
            ImageDetailScreen(
                state = ImageDetailUiState(
                    isLoading = false,
                    image = image,
                    error = false,
                ),
                onAction = {},
            )
        }

        composeTestRule
            .onNodeWithText(image.title)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(image.title)
            .assertExists()
    }

    @Test
    fun whenTopBarIsRendered_displaysTitleAndBackButtonTriggersOnBack() {
        var backClicked = false

        composeTestRule.setContent {
            ImageDetailTopBar {
                backClicked = true
            }
        }

        composeTestRule
            .onNodeWithText(
                targetContext.getString(R.string.image_detail_title),
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription(
                targetContext.getString(R.string.content_back),
            )
            .performClick()

        assertTrue(backClicked)
    }
}
