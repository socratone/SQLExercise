package io.github.socratone.sqlexercise

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import io.github.socratone.sqlexercise.ui.LevelDetailScreen
import io.github.socratone.sqlexercise.ui.LevelExercise
import io.github.socratone.sqlexercise.ui.SubmissionResult
import io.github.socratone.sqlexercise.ui.theme.SQLExerciseTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LevelDetailScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val exercise = LevelExercise(
        id = 1,
        levelTitle = "1 level",
        question = "users 테이블의 모든 데이터를 조회하세요.",
        expectedSql = "SELECT * FROM users",
    )

    @Test
    fun showsProblemAndDisablesSubmitForBlankInput() {
        composeRule.setContent {
            SQLExerciseTheme {
                LevelDetailScreen(
                    exercise = exercise,
                    sqlInput = "",
                    submissionResult = SubmissionResult.NotSubmitted,
                    onInputChange = {},
                    onReset = {},
                    onSubmit = {},
                )
            }
        }

        composeRule.onNodeWithText(exercise.question).assertIsDisplayed()
        composeRule.onNodeWithText("제출").assertIsNotEnabled()
    }

    @Test
    fun resetButtonCallsResetCallback() {
        var resetCalled = false
        composeRule.setContent {
            SQLExerciseTheme {
                LevelDetailScreen(
                    exercise = exercise,
                    sqlInput = "SELECT * FROM users",
                    submissionResult = SubmissionResult.Correct,
                    onInputChange = {},
                    onReset = { resetCalled = true },
                    onSubmit = {},
                )
            }
        }

        composeRule.onNodeWithText("초기화").performClick()
        assertTrue(resetCalled)
    }

    @Test
    fun showsQueryErrorsSeparatelyFromIncorrectAnswers() {
        composeRule.setContent {
            SQLExerciseTheme {
                LevelDetailScreen(
                    exercise = exercise,
                    sqlInput = "SELECT FROM users",
                    submissionResult = SubmissionResult.QueryError("SQL 실행 오류: 문법을 확인해 주세요."),
                    onInputChange = {},
                    onReset = {},
                    onSubmit = {},
                )
            }
        }

        composeRule.onNodeWithText("SQL 실행 오류: 문법을 확인해 주세요.").assertIsDisplayed()
    }
}
