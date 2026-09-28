package com.example.contentexplorer.feature.home.ui

import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.contentexplorer.core.domain.model.ChoiceQuestion
import com.example.contentexplorer.core.domain.model.ImageQuestion
import com.example.contentexplorer.core.domain.model.Page
import com.example.contentexplorer.core.domain.model.Response
import com.example.contentexplorer.core.domain.model.ResponseSet
import com.example.contentexplorer.core.domain.model.Section
import com.example.contentexplorer.core.domain.model.TextQuestion
import com.example.contentexplorer.feature.home.R
import com.example.contentexplorer.feature.home.presentation.HomeAction
import com.example.contentexplorer.feature.home.presentation.HomeError
import com.example.contentexplorer.feature.home.presentation.HomeUiState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val targetContext = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun whenLoadingWithoutContent_displaysLoadingIndicator() {
        composeTestRule.setContent {
            HomeScreen(
                state = HomeUiState(isLoading = true),
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
    fun whenContentIsEmpty_displaysEmptyState() {
        composeTestRule.setContent {
            HomeScreen(
                state = HomeUiState(),
                onAction = {},
            )
        }

        composeTestRule
            .onNodeWithText(
                targetContext.getString(R.string.home_empty_state),
            )
            .assertIsDisplayed()
    }

    @Test
    fun whenContentIsEmptyAndNetworkError_displaysErrorAndRetryDispatchesRefresh() {
        val actions = mutableListOf<HomeAction>()

        composeTestRule.setContent {
            HomeScreen(
                state = HomeUiState(
                    error = HomeError.Network,
                ),
                onAction = actions::add,
            )
        }

        composeTestRule
            .onNodeWithText(
                targetContext.getString(R.string.home_error_network),
            )
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                targetContext.getString(R.string.content_retry),
            )
            .performClick()

        assertEquals(
            listOf(HomeAction.Refresh),
            actions,
        )
    }

    @Test
    fun whenContentIsAvailable_displaysPageHierarchyAndQuestions() {
        composeTestRule.setContent {
            HomeScreen(
                state = HomeUiState(
                    data = listOf(testPage),
                ),
                onAction = {},
            )
        }

        composeTestRule.onNodeWithText("Main Page").assertIsDisplayed()
        composeTestRule.onNodeWithText("Introduction").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nested Section").assertIsDisplayed()
        composeTestRule.onNodeWithText("Welcome to the main page!")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Welcome Image")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("What is the main topic of Chapter 2?")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Which areas were inspected?")
            .assertIsDisplayed()
    }

    @Test
    fun whenImageIsClicked_dispatchesImageClickedAction() {
        val actions = mutableListOf<HomeAction>()

        composeTestRule.setContent {
            HomeScreen(
                state = HomeUiState(
                    data = listOf(testPage),
                ),
                onAction = actions::add,
            )
        }

        composeTestRule
            .onNodeWithContentDescription("Welcome Image")
            .performClick()

        assertEquals(
            listOf(
                HomeAction.ImageClicked(imageId = 4L),
            ),
            actions,
        )
    }

    @Test
    fun whenSingleChoiceIsClicked_dispatchesResponseSelectedAction() {
        val actions = mutableListOf<HomeAction>()

        composeTestRule.setContent {
            HomeScreen(
                state = HomeUiState(
                    data = listOf(testPage),
                ),
                onAction = actions::add,
            )
        }

        composeTestRule
            .onNodeWithText("Option 1")
            .performScrollTo()
            .performClick()

        assertEquals(
            listOf(
                HomeAction.ResponseSelected(
                    questionId = 13L,
                    responseId = 1011L,
                ),
            ),
            actions,
        )
    }

    @Test
    fun whenMultipleChoiceResponsesAreClicked_dispatchesSelectionActions() {
        val actions = mutableListOf<HomeAction>()

        composeTestRule.setContent {
            HomeScreen(
                state = HomeUiState(
                    data = listOf(testPage),
                ),
                onAction = actions::add,
            )
        }

        composeTestRule
            .onNodeWithText("Entrance")
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNodeWithText("Storage")
            .performScrollTo()
            .performClick()

        assertEquals(
            listOf(
                HomeAction.ResponseSelected(
                    questionId = 14L,
                    responseId = 1021L,
                ),
                HomeAction.ResponseSelected(
                    questionId = 14L,
                    responseId = 1022L,
                ),
            ),
            actions,
        )
    }

    @Test
    fun whenSingleChoiceHasSelectedResponse_displaysSelectedRadioButton() {
        composeTestRule.setContent {
            HomeScreen(
                state = HomeUiState(
                    data = listOf(testPage),
                    selectedResponses = mapOf(
                        13L to setOf(1011L),
                    ),
                ),
                onAction = {},
            )
        }

        composeTestRule
            .onAllNodes(hasRole(Role.RadioButton))[0]
            .performScrollTo()
            .assertIsSelected()
    }

    @Test
    fun whenMultipleChoiceHasSelectedResponses_displaysSelectedCheckboxes() {
        composeTestRule.setContent {
            HomeScreen(
                state = HomeUiState(
                    data = listOf(testPage),
                    selectedResponses = mapOf(
                        14L to setOf(1021L, 1022L),
                    ),
                ),
                onAction = {},
            )
        }

        composeTestRule
            .onAllNodes(hasRole(Role.Checkbox))[0]
            .performScrollTo()
            .assertIsOn()

        composeTestRule
            .onAllNodes(hasRole(Role.Checkbox))[1]
            .performScrollTo()
            .assertIsOn()
    }

    @Test
    fun whenContentIsAvailableAndErrorExists_displaysContentAndError() {
        composeTestRule.setContent {
            HomeScreen(
                state = HomeUiState(
                    data = listOf(
                        testPage.copy(title = "Cached Page"),
                    ),
                    error = HomeError.Network,
                ),
                onAction = {},
            )
        }

        composeTestRule
            .onNodeWithText("Cached Page")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                targetContext.getString(R.string.home_error_network),
            )
            .assertIsDisplayed()
    }

    private fun hasRole(role: Role): SemanticsMatcher =
        SemanticsMatcher.expectValue(SemanticsProperties.Role, role)

    private companion object {

        val testPage = Page(
            id = 1L,
            title = "Main Page",
            items = listOf(
                Section(
                    id = 2L,
                    title = "Introduction",
                    items = listOf(
                        TextQuestion(
                            id = 3L,
                            content = "Welcome to the main page!",
                        ),
                        ImageQuestion(
                            id = 4L,
                            src = "https://example.com/android.png",
                            title = "Welcome Image",
                        ),
                        Section(
                            id = 5L,
                            title = "Nested Section",
                            items = listOf(
                                TextQuestion(
                                    id = 6L,
                                    content = "Nested content",
                                ),
                            ),
                        ),
                    ),
                ),
                Section(
                    id = 11L,
                    title = "Chapter 2",
                    items = listOf(
                        ChoiceQuestion(
                            id = 13L,
                            content = "What is the main topic of Chapter 2?",
                            responseSet = ResponseSet(
                                id = 101L,
                                multipleSelection = false,
                                responses = listOf(
                                    Response(
                                        id = 1011L,
                                        label = "Option 1",
                                        score = 1,
                                    ),
                                    Response(
                                        id = 1012L,
                                        label = "Option 2",
                                        score = 2,
                                    ),
                                    Response(
                                        id = 1013L,
                                        label = "Option 3",
                                        score = null,
                                    ),
                                ),
                            ),
                        ),
                        ChoiceQuestion(
                            id = 14L,
                            content = "Which areas were inspected?",
                            responseSet = ResponseSet(
                                id = 102L,
                                multipleSelection = true,
                                responses = listOf(
                                    Response(
                                        id = 1021L,
                                        label = "Entrance",
                                        score = null,
                                    ),
                                    Response(
                                        id = 1022L,
                                        label = "Storage",
                                        score = null,
                                    ),
                                    Response(
                                        id = 1023L,
                                        label = "Loading bay",
                                        score = null,
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        )
    }
}