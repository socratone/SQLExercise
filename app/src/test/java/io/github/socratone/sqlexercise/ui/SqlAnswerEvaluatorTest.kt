package io.github.socratone.sqlexercise.ui

import org.junit.Assert.assertEquals
import org.junit.Test

class SqlAnswerEvaluatorTest {
    @Test
    fun ignoresCaseWhitespaceAndTrailingSemicolon() {
        val result = evaluateSqlAnswer(
            input = "  select   *\nFROM users;  ",
            expected = "SELECT * FROM users",
        )

        assertEquals(SubmissionResult.Correct, result)
    }

    @Test
    fun returnsIncorrectForDifferentSql() {
        val result = evaluateSqlAnswer(
            input = "SELECT name FROM users",
            expected = "SELECT * FROM users",
        )

        assertEquals(SubmissionResult.Incorrect, result)
    }
}
