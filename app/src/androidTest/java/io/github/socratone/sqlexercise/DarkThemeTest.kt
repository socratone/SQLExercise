package io.github.socratone.sqlexercise

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import io.github.socratone.sqlexercise.ui.theme.SQLExerciseTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DarkThemeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun alwaysUsesFixedDarkColors() {
        var background = Color.Unspecified
        var content = Color.Unspecified

        composeRule.setContent {
            SQLExerciseTheme {
                background = MaterialTheme.colorScheme.background
                content = MaterialTheme.colorScheme.onBackground
            }
        }

        composeRule.runOnIdle {
            assertEquals(Color(0xFF0B0B0B), background)
            assertEquals(Color(0xFFF5F5F5), content)
        }
    }
}
