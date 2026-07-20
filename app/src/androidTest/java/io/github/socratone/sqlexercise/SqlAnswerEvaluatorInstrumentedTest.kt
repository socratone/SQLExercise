package io.github.socratone.sqlexercise

import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.socratone.sqlexercise.ui.SubmissionResult
import io.github.socratone.sqlexercise.ui.evaluateSqlAnswer
import io.github.socratone.sqlexercise.ui.sampleExercises
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SqlAnswerEvaluatorInstrumentedTest {
    @Test
    fun allSampleAnswersMatchTheirResults() {
        sampleExercises.forEach { exercise ->
            assertEquals(
                "Expected level ${exercise.id} to be correct",
                SubmissionResult.Correct,
                evaluateSqlAnswer(
                    input = exercise.expectedSql,
                    expected = exercise.expectedSql,
                    orderSensitive = exercise.orderSensitive,
                ),
            )
        }
    }

    @Test
    fun acceptsDifferentSqlThatReturnsTheSameResult() {
        val result = evaluateSqlAnswer(
            input = "SELECT id, name, age, city FROM users",
            expected = "SELECT * FROM users",
            orderSensitive = false,
        )

        assertEquals(SubmissionResult.Correct, result)
    }

    @Test
    fun rejectsIncorrectResultAndIncorrectOrder() {
        assertEquals(
            SubmissionResult.Incorrect,
            evaluateSqlAnswer(
                input = "SELECT name FROM users",
                expected = "SELECT city FROM users",
                orderSensitive = false,
            ),
        )
        assertEquals(
            SubmissionResult.Incorrect,
            evaluateSqlAnswer(
                input = "SELECT * FROM users ORDER BY id",
                expected = "SELECT * FROM users ORDER BY name",
                orderSensitive = true,
            ),
        )
    }

    @Test
    fun reportsSyntaxAndNonQueryStatementsAsErrors() {
        val syntaxError = evaluateSqlAnswer(
            input = "SELECT FROM users",
            expected = "SELECT * FROM users",
            orderSensitive = false,
        )
        val writeStatement = evaluateSqlAnswer(
            input = "DELETE FROM users",
            expected = "SELECT * FROM users",
            orderSensitive = false,
        )
        val multipleStatements = evaluateSqlAnswer(
            input = "SELECT * FROM users; SELECT * FROM orders",
            expected = "SELECT * FROM users",
            orderSensitive = false,
        )

        assertTrue(syntaxError is SubmissionResult.QueryError)
        assertTrue(writeStatement is SubmissionResult.QueryError)
        assertTrue(multipleStatements is SubmissionResult.QueryError)
    }
}
