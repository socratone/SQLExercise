package io.github.socratone.sqlexercise

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.socratone.sqlexercise.data.ExerciseProgressStore
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExerciseProgressStoreTest {
    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    private val preferencesName = "exercise_progress_test"

    @Before
    @After
    fun clearPreferences() {
        context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .commit()
    }

    @Test
    fun completedExerciseIdsSurviveStoreRecreationWithoutDuplicates() {
        val firstStore = ExerciseProgressStore(context, preferencesName)
        firstStore.markCompleted(3)
        firstStore.markCompleted(3)
        firstStore.markCompleted(7)

        val recreatedStore = ExerciseProgressStore(context, preferencesName)

        assertEquals(setOf(3, 7), recreatedStore.loadCompletedExerciseIds())
    }
}
