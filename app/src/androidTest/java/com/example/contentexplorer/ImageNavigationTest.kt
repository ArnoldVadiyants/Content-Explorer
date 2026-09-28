package com.example.contentexplorer

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
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

        val homeContent = composeTestRule.onNodeWithTag("home_content")
        composeTestRule.waitUntil { homeContent.isDisplayed() }

        val imageQuestion = composeTestRule
            .onAllNodesWithTag("image_question_image")
            .onFirst()
        composeTestRule.waitUntil { imageQuestion.isDisplayed() }

        imageQuestion.performClick()

        val detailScreen = composeTestRule.onNodeWithTag("image_detail_content")
        composeTestRule.waitUntil { detailScreen.isDisplayed() }
    }
}