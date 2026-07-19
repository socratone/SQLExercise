package io.github.socratone.sqlexercise

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.socratone.sqlexercise.ui.LevelListScreen
import io.github.socratone.sqlexercise.ui.LevelSummary
import io.github.socratone.sqlexercise.ui.theme.SQLExerciseTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LevelListScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun displaysLevelsAndPassesSelectedId() {
        var selectedId: Int? = null
        val levels = (1..10).map { LevelSummary(it, "$it level") }

        composeRule.setContent {
            SQLExerciseTheme {
                LevelListScreen(
                    levels = levels,
                    onLevelClick = { selectedId = it },
                )
            }
        }

        composeRule.onNodeWithText("1 level").assertIsDisplayed().performClick()
        assertEquals(1, selectedId)
        composeRule.onNodeWithText("10 level").assertExists()
    }
}
