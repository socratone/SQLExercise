package io.github.socratone.sqlexercise

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.TextFieldValue
import io.github.socratone.sqlexercise.ui.LevelDetailRoute
import io.github.socratone.sqlexercise.ui.LevelDetailScreen
import io.github.socratone.sqlexercise.ui.LevelDetailUiState
import io.github.socratone.sqlexercise.ui.LevelExercise
import io.github.socratone.sqlexercise.ui.SqlInputAccessoryBar
import io.github.socratone.sqlexercise.ui.SubmissionResult
import io.github.socratone.sqlexercise.ui.theme.SQLExerciseTheme
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
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
                    sqlInput = TextFieldValue(),
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
                    sqlInput = TextFieldValue("SELECT * FROM employees"),
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
                    sqlInput = TextFieldValue("SELECT FROM employees"),
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
                    sqlInput = TextFieldValue(),
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
                    sqlInput = TextFieldValue(),
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

    @Test
    fun inputAccessoryShowsCategoriesAndKeywordTokens() {
        var selectedToken: String? = null
        var selectedTableCategory: Boolean? = null
        composeRule.setContent {
            SQLExerciseTheme {
                SqlInputAccessoryBar(
                    showTableTokens = false,
                    onCategorySelected = { selectedTableCategory = it },
                    onTokenClick = { selectedToken = it },
                )
            }
        }

        composeRule.onNodeWithText("키워드").assertIsDisplayed()
        composeRule.onNodeWithText("테이블").assertIsDisplayed()
        composeRule.onNodeWithText("SELECT").performClick()
        composeRule.onNodeWithText("테이블").performClick()

        composeRule.runOnIdle {
            assertTrue(selectedToken == "SELECT")
            assertTrue(selectedTableCategory == true)
        }
    }

    @Test
    fun inputAccessoryShowsAndSelectsTableTokens() {
        var selectedToken: String? = null
        composeRule.setContent {
            SQLExerciseTheme {
                SqlInputAccessoryBar(
                    showTableTokens = true,
                    onCategorySelected = {},
                    onTokenClick = { selectedToken = it },
                )
            }
        }

        composeRule.onNodeWithText("employees").performScrollTo().performClick()

        composeRule.runOnIdle {
            assertTrue(selectedToken == "employees")
        }
    }

    @Test
    fun correctSubmissionReportsCompletedExercise() {
        var completedExerciseId: Int? = null
        composeRule.setContent {
            SQLExerciseTheme {
                LevelDetailRoute(
                    uiState = LevelDetailUiState.Content(exercise),
                    onBackClick = {},
                    onExerciseCompleted = { completedExerciseId = it },
                )
            }
        }

        composeRule.onNodeWithText("SQL을 입력하세요").performTextInput(exercise.expectedSql)
        composeRule.onNodeWithText("제출").performClick()

        composeRule.runOnIdle {
            assertTrue(completedExerciseId == exercise.id)
        }
    }

    @Test
    fun previousAndNextButtonsCallTheirCallbacks() {
        var previousCalled = false
        var nextCalled = false
        composeRule.setContent {
            SQLExerciseTheme {
                LevelDetailScreen(
                    exercise = exercise,
                    sqlInput = TextFieldValue(),
                    submissionResult = SubmissionResult.NotSubmitted,
                    onInputChange = {},
                    onReset = {},
                    onSubmit = {},
                    previousEnabled = true,
                    nextEnabled = true,
                    onPreviousClick = { previousCalled = true },
                    onNextClick = { nextCalled = true },
                )
            }
        }

        composeRule.onNodeWithContentDescription("이전 문제").assertIsEnabled().performClick()
        composeRule.onNodeWithContentDescription("다음 문제").assertIsEnabled().performClick()

        composeRule.runOnIdle {
            assertTrue(previousCalled)
            assertTrue(nextCalled)
        }
    }

    @Test
    fun disablesNavigationButtonsAtListBoundaries() {
        var previousCalled = false
        var nextCalled = false
        composeRule.setContent {
            SQLExerciseTheme {
                LevelDetailScreen(
                    exercise = exercise,
                    sqlInput = TextFieldValue(),
                    submissionResult = SubmissionResult.NotSubmitted,
                    onInputChange = {},
                    onReset = {},
                    onSubmit = {},
                    previousEnabled = false,
                    nextEnabled = false,
                    onPreviousClick = { previousCalled = true },
                    onNextClick = { nextCalled = true },
                )
            }
        }

        composeRule.onNodeWithContentDescription("이전 문제").assertIsNotEnabled()
        composeRule.onNodeWithContentDescription("다음 문제").assertIsNotEnabled()
        composeRule.runOnIdle {
            assertFalse(previousCalled)
            assertFalse(nextCalled)
        }
    }
}
