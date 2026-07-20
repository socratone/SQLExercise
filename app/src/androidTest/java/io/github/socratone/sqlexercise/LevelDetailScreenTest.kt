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
        levelTitle = "문제 1 · 전체 직원 조회",
        question = "employees 테이블의 모든 데이터를 조회하세요.",
        expectedSql = "SELECT * FROM employees",
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
                    sqlInput = "SELECT * FROM employees",
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
                    sqlInput = "SELECT FROM employees",
                    submissionResult = SubmissionResult.QueryError("SQL 실행 오류: 문법을 확인해 주세요."),
                    onInputChange = {},
                    onReset = {},
                    onSubmit = {},
                )
            }
        }

        composeRule.onNodeWithText("SQL 실행 오류: 문법을 확인해 주세요.").assertIsDisplayed()
    }

    @Test
    fun expandsSchemaReference() {
        var expanded = false
        composeRule.setContent {
            SQLExerciseTheme {
                LevelDetailScreen(
                    exercise = exercise,
                    sqlInput = "",
                    submissionResult = SubmissionResult.NotSubmitted,
                    onInputChange = {},
                    onReset = {},
                    onSubmit = {},
                    schemaExpanded = expanded,
                    onSchemaToggle = { expanded = true },
                )
            }
        }

        composeRule.onNodeWithText("스키마 보기").performClick()
        composeRule.runOnIdle { assertTrue(expanded) }
    }

    @Test
    fun showsSchemaContentsWhenExpanded() {
        composeRule.setContent {
            SQLExerciseTheme {
                LevelDetailScreen(
                    exercise = exercise,
                    sqlInput = "",
                    submissionResult = SubmissionResult.NotSubmitted,
                    onInputChange = {},
                    onReset = {},
                    onSubmit = {},
                    schemaExpanded = true,
                )
            }
        }

        composeRule.onNodeWithText("HR 데이터베이스 스키마").assertIsDisplayed()
        composeRule.onNodeWithText("스키마 접기").assertIsDisplayed()
    }
}
