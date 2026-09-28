package com.example.contentexplorer

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageNavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun whenImageIsClicked_opensImageDetailScreen() {
        composeTestRule
            .onNodeWithText("Main Page")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Welcome Image")
            .assertIsDisplayed()
            .performClick()

        composeTestRule
            .onNodeWithTag(
                "image_detail_contet"
            )
            .assertIsDisplayed()
    }
}