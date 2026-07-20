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
            input = "SELECT employee_id, first_name, last_name, email, phone_number, hire_date, job_id, salary, commission_pct, manager_id, department_id FROM employees",
            expected = "SELECT * FROM employees",
            orderSensitive = false,
        )

        assertEquals(SubmissionResult.Correct, result)
    }

    @Test
    fun rejectsIncorrectResultAndIncorrectOrder() {
        assertEquals(
            SubmissionResult.Incorrect,
            evaluateSqlAnswer(
                input = "SELECT first_name FROM employees",
                expected = "SELECT last_name FROM employees",
                orderSensitive = false,
            ),
        )
        assertEquals(
            SubmissionResult.Incorrect,
            evaluateSqlAnswer(
                input = "SELECT * FROM employees ORDER BY employee_id",
                expected = "SELECT * FROM employees ORDER BY salary DESC, employee_id",
                orderSensitive = true,
            ),
        )
    }

    @Test
    fun reportsSyntaxAndNonQueryStatementsAsErrors() {
        val syntaxError = evaluateSqlAnswer(
            input = "SELECT FROM employees",
            expected = "SELECT * FROM employees",
            orderSensitive = false,
        )
        val writeStatement = evaluateSqlAnswer(
            input = "DELETE FROM employees",
            expected = "SELECT * FROM employees",
            orderSensitive = false,
        )
        val multipleStatements = evaluateSqlAnswer(
            input = "SELECT * FROM employees; SELECT * FROM jobs",
            expected = "SELECT * FROM employees",
            orderSensitive = false,
        )

        assertTrue(syntaxError is SubmissionResult.QueryError)
        assertTrue(writeStatement is SubmissionResult.QueryError)
        assertTrue(multipleStatements is SubmissionResult.QueryError)
    }
}
