package io.github.socratone.sqlexercise

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.socratone.sqlexercise.data.createHrDatabase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HrDatabaseFixtureTest {
    @Test
    fun createsExpectedHrDatasetWithValidForeignKeys() {
        BundledSQLiteDriver().open(":memory:").use { connection ->
            connection.createHrDatabase()

            assertEquals(4, connection.singleLong("SELECT COUNT(*) FROM regions"))
            assertEquals(10, connection.singleLong("SELECT COUNT(*) FROM countries"))
            assertEquals(12, connection.singleLong("SELECT COUNT(*) FROM locations"))
            assertEquals(12, connection.singleLong("SELECT COUNT(*) FROM departments"))
            assertEquals(15, connection.singleLong("SELECT COUNT(*) FROM jobs"))
            assertEquals(50, connection.singleLong("SELECT COUNT(*) FROM employees"))
            assertEquals(30, connection.singleLong("SELECT COUNT(*) FROM job_history"))
            assertEquals(0, connection.singleLong("SELECT COUNT(*) FROM pragma_foreign_key_check"))
        }
    }
}

private fun androidx.sqlite.SQLiteConnection.singleLong(sql: String): Long =
    prepare(sql).use { statement ->
        check(statement.step())
        statement.getLong(0)
    }
