package io.github.socratone.sqlexercise.ui

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import org.junit.Assert.assertEquals
import org.junit.Test

class SqlTokenInsertionTest {
    @Test
    fun insertsTokenAndTrailingSpaceIntoEmptyInput() {
        val result = insertSqlToken(TextFieldValue(), "SELECT")

        assertEquals("SELECT ", result.text)
        assertEquals(TextRange(7), result.selection)
    }

    @Test
    fun insertsTokenAtCurrentCursor() {
        val input = TextFieldValue(
            text = "SELECT  FROM employees",
            selection = TextRange(7),
        )

        val result = insertSqlToken(input, "employees")

        assertEquals("SELECT employees FROM employees", result.text)
        assertEquals(TextRange(16), result.selection)
    }

    @Test
    fun replacesSelectionAndKeepsPunctuationTight() {
        val input = TextFieldValue(
            text = "SELECT departments;",
            selection = TextRange(7, 18),
        )

        val result = insertSqlToken(input, "employees")

        assertEquals("SELECT employees;", result.text)
        assertEquals(TextRange(16), result.selection)
    }

    @Test
    fun doesNotInsertSpaceAfterOpeningParenthesis() {
        val input = TextFieldValue(
            text = "WHERE EXISTS ()",
            selection = TextRange(14),
        )

        val result = insertSqlToken(input, "SELECT")

        assertEquals("WHERE EXISTS (SELECT)", result.text)
        assertEquals(TextRange(20), result.selection)
    }

    @Test
    fun insertsSymbolWithoutAddingSpaces() {
        val input = TextFieldValue(
            text = "LIKE 'A'",
            selection = TextRange(7),
        )

        val result = insertSqlSymbol(input, "_")

        assertEquals("LIKE 'A_'", result.text)
        assertEquals(TextRange(8), result.selection)
    }
}
