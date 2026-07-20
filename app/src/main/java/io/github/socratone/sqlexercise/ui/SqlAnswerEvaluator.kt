package io.github.socratone.sqlexercise.ui

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import java.math.BigDecimal

/** SQLite에서 반환할 수 있는 값을 플랫폼 타입과 분리해 표현합니다. */
internal sealed interface SqlValue {
    data object Null : SqlValue
    data class Integer(val value: Long) : SqlValue
    data class Real(val value: Double) : SqlValue
    data class Text(val value: String) : SqlValue
    data class Blob(val value: List<Byte>) : SqlValue
}

/** 빈 결과에서도 열 개수를 비교할 수 있도록 메타데이터와 행을 함께 보관합니다. */
internal data class SqlQueryResult(
    val columnCount: Int,
    val rows: List<List<SqlValue>>,
)

/**
 * 정답 SQL과 사용자 SQL을 동일한 인메모리 데이터베이스에서 실행한 뒤 결과를 비교합니다.
 * 데이터베이스는 제출할 때마다 새로 생성되므로 제출 사이에 상태가 공유되지 않습니다.
 */
internal fun evaluateSqlAnswer(
    input: String,
    expected: String,
    orderSensitive: Boolean,
): SubmissionResult {
    val validatedInput = validateQuery(input)
        ?: return SubmissionResult.QueryError(
            "SELECT 또는 WITH로 시작하는 하나의 조회 쿼리만 입력해 주세요.",
        )
    val validatedExpected = validateQuery(expected)
        ?: return SubmissionResult.QueryError("채점 데이터를 준비하지 못했습니다.")

    return try {
        SQLiteDatabase.create(null).use { database ->
            createSampleDatabase(database)
            database.execSQL("PRAGMA query_only = ON")

            val expectedResult = database.readQuery(validatedExpected)
            val inputResult = try {
                database.readQuery(validatedInput)
            } catch (error: SQLiteException) {
                return SubmissionResult.QueryError(
                    error.message?.let { "SQL 실행 오류: $it" } ?: "SQL을 실행할 수 없습니다.",
                )
            }

            if (resultsAreEqual(expectedResult, inputResult, orderSensitive)) {
                SubmissionResult.Correct
            } else {
                SubmissionResult.Incorrect
            }
        }
    } catch (_: SQLiteException) {
        SubmissionResult.QueryError("채점 데이터를 준비하지 못했습니다.")
    }
}

/** 열 위치와 중복 행은 보존하고, 설정에 따라 행 순서만 무시합니다. */
internal fun resultsAreEqual(
    expected: SqlQueryResult,
    actual: SqlQueryResult,
    orderSensitive: Boolean,
): Boolean {
    if (expected.columnCount != actual.columnCount || expected.rows.size != actual.rows.size) {
        return false
    }

    if (orderSensitive) {
        return expected.rows.zip(actual.rows).all { (expectedRow, actualRow) ->
            rowsAreEqual(expectedRow, actualRow)
        }
    }

    val matchedActualRows = BooleanArray(actual.rows.size)
    return expected.rows.all { expectedRow ->
        val matchingIndex = actual.rows.indices.firstOrNull { index ->
            !matchedActualRows[index] && rowsAreEqual(expectedRow, actual.rows[index])
        } ?: return@all false

        matchedActualRows[matchingIndex] = true
        true
    }
}

private fun rowsAreEqual(expected: List<SqlValue>, actual: List<SqlValue>): Boolean =
    expected.size == actual.size && expected.zip(actual).all { (expectedValue, actualValue) ->
        valuesAreEqual(expectedValue, actualValue)
    }

private fun valuesAreEqual(expected: SqlValue, actual: SqlValue): Boolean = when {
    expected is SqlValue.Integer && actual is SqlValue.Real ->
        integerAndRealAreEqual(expected.value, actual.value)
    expected is SqlValue.Real && actual is SqlValue.Integer ->
        integerAndRealAreEqual(actual.value, expected.value)
    else -> expected == actual
}

private fun integerAndRealAreEqual(integer: Long, real: Double): Boolean =
    real.isFinite() && BigDecimal.valueOf(integer).compareTo(BigDecimal.valueOf(real)) == 0

private fun SQLiteDatabase.readQuery(sql: String): SqlQueryResult =
    rawQuery(sql, null).use { cursor ->
        val rows = buildList {
            while (cursor.moveToNext()) {
                add(cursor.readRow())
            }
        }
        SqlQueryResult(columnCount = cursor.columnCount, rows = rows)
    }

private fun Cursor.readRow(): List<SqlValue> = List(columnCount) { columnIndex ->
    when (getType(columnIndex)) {
        Cursor.FIELD_TYPE_NULL -> SqlValue.Null
        Cursor.FIELD_TYPE_INTEGER -> SqlValue.Integer(getLong(columnIndex))
        Cursor.FIELD_TYPE_FLOAT -> SqlValue.Real(getDouble(columnIndex))
        Cursor.FIELD_TYPE_STRING -> SqlValue.Text(getString(columnIndex))
        Cursor.FIELD_TYPE_BLOB -> SqlValue.Blob(getBlob(columnIndex).toList())
        else -> error("Unsupported SQLite value type")
    }
}

private fun createSampleDatabase(database: SQLiteDatabase) {
    database.execSQL(
        """
        CREATE TABLE users (
            id INTEGER PRIMARY KEY,
            name TEXT NOT NULL,
            age INTEGER NOT NULL,
            city TEXT
        )
        """.trimIndent(),
    )
    database.execSQL(
        """
        CREATE TABLE orders (
            id INTEGER PRIMARY KEY,
            user_id INTEGER NOT NULL,
            amount REAL NOT NULL,
            FOREIGN KEY (user_id) REFERENCES users(id)
        )
        """.trimIndent(),
    )

    listOf(
        "INSERT INTO users VALUES (1, 'Charlie', 25, 'Seoul')",
        "INSERT INTO users VALUES (2, 'Alice', 19, 'Busan')",
        "INSERT INTO users VALUES (3, 'Eve', 32, 'Seoul')",
        "INSERT INTO users VALUES (4, 'Bob', 20, NULL)",
        "INSERT INTO users VALUES (5, 'Dana', 21, 'Busan')",
        "INSERT INTO orders VALUES (1, 1, 150.0)",
        "INSERT INTO orders VALUES (2, 1, 50.0)",
        "INSERT INTO orders VALUES (3, 2, 200.0)",
        "INSERT INTO orders VALUES (4, 3, 120.0)",
        "INSERT INTO orders VALUES (5, 3, 100.0)",
        "INSERT INTO orders VALUES (6, 5, 101.0)",
    ).forEach(database::execSQL)
}

/**
 * 문자열 리터럴과 SQL 주석 안의 세미콜론은 허용하되, 실제 문장은 하나만 허용합니다.
 * 이 검사는 허용할 문장 종류를 좁히고 실제 문법 검증은 SQLite에 맡깁니다.
 */
private fun validateQuery(sql: String): String? {
    val content = sql.trim()
    if (content.isEmpty()) return null

    val semicolons = statementSemicolonIndexes(content)
    if (semicolons.size > 1 || (semicolons.size == 1 && semicolons.single() != content.lastIndex)) {
        return null
    }

    val withoutTrailingSemicolon = if (semicolons.singleOrNull() == content.lastIndex) {
        content.dropLast(1).trimEnd()
    } else {
        content
    }
    val firstKeyword = withoutTrailingSemicolon
        .replace(Regex("(?s)\\A(?:\\s|--[^\\n]*(?:\\n|\\z)|/\\*.*?\\*/)*"), "")
        .takeWhile { it.isLetter() }
        .uppercase()

    return withoutTrailingSemicolon.takeIf { firstKeyword == "SELECT" || firstKeyword == "WITH" }
}

private fun statementSemicolonIndexes(sql: String): List<Int> {
    val indexes = mutableListOf<Int>()
    var index = 0
    var quote: Char? = null
    var inLineComment = false
    var inBlockComment = false

    while (index < sql.length) {
        val current = sql[index]
        val next = sql.getOrNull(index + 1)
        when {
            inLineComment && current == '\n' -> inLineComment = false
            inBlockComment && current == '*' && next == '/' -> {
                inBlockComment = false
                index++
            }
            inLineComment || inBlockComment -> Unit
            quote != null && current == quote && next == quote -> index++
            quote != null && current == quote -> quote = null
            quote != null -> Unit
            current == '-' && next == '-' -> {
                inLineComment = true
                index++
            }
            current == '/' && next == '*' -> {
                inBlockComment = true
                index++
            }
            current == '\'' || current == '"' -> quote = current
            current == ';' -> indexes += index
        }
        index++
    }
    return indexes
}
