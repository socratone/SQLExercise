package io.github.socratone.sqlexercise.ui

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SqlAnswerEvaluatorTest {
    @Test
    fun ignoresRowOrderWhenOrderIsNotSignificant() {
        val firstRow = listOf(SqlValue.Integer(1), SqlValue.Text("Alice"))
        val secondRow = listOf(SqlValue.Integer(2), SqlValue.Text("Bob"))

        assertTrue(
            resultsAreEqual(
                expected = SqlQueryResult(2, listOf(firstRow, secondRow)),
                actual = SqlQueryResult(2, listOf(secondRow, firstRow)),
                orderSensitive = false,
            ),
        )
    }

    @Test
    fun comparesRowOrderWhenOrderIsSignificant() {
        val firstRow = listOf(SqlValue.Integer(1))
        val secondRow = listOf(SqlValue.Integer(2))

        assertFalse(
            resultsAreEqual(
                expected = SqlQueryResult(1, listOf(firstRow, secondRow)),
                actual = SqlQueryResult(1, listOf(secondRow, firstRow)),
                orderSensitive = true,
            ),
        )
    }

    @Test
    fun preservesDuplicateRowCounts() {
        val row = listOf(SqlValue.Text("Seoul"))

        assertFalse(
            resultsAreEqual(
                expected = SqlQueryResult(1, listOf(row, row)),
                actual = SqlQueryResult(1, listOf(row, listOf(SqlValue.Text("Busan")))),
                orderSensitive = false,
            ),
        )
    }

    @Test
    fun treatsEquivalentIntegerAndRealValuesAsEqual() {
        assertTrue(
            resultsAreEqual(
                expected = SqlQueryResult(1, listOf(listOf(SqlValue.Integer(1)))),
                actual = SqlQueryResult(1, listOf(listOf(SqlValue.Real(1.0)))),
                orderSensitive = false,
            ),
        )
    }

    @Test
    fun rejectsDifferentColumnCountsEvenForEmptyResults() {
        assertFalse(
            resultsAreEqual(
                expected = SqlQueryResult(columnCount = 1, rows = emptyList()),
                actual = SqlQueryResult(columnCount = 2, rows = emptyList()),
                orderSensitive = false,
            ),
        )
    }
}
