package io.github.socratone.sqlexercise.data

import android.content.Context

/** 기기 로컬에 정답을 맞힌 문제 ID를 보관합니다. */
class ExerciseProgressStore(
    context: Context,
    preferencesName: String = DEFAULT_PREFERENCES_NAME,
) {
    private val preferences = context.applicationContext.getSharedPreferences(
        preferencesName,
        Context.MODE_PRIVATE,
    )

    @Synchronized
    fun loadCompletedExerciseIds(): Set<Int> =
        preferences.getStringSet(COMPLETED_EXERCISE_IDS_KEY, emptySet())
            .orEmpty()
            .mapNotNull(String::toIntOrNull)
            .toSet()

    @Synchronized
    fun markCompleted(exerciseId: Int) {
        val updatedIds = loadCompletedExerciseIds() + exerciseId
        preferences.edit()
            .putStringSet(COMPLETED_EXERCISE_IDS_KEY, updatedIds.map(Int::toString).toSet())
            .apply()
    }

    private companion object {
        const val DEFAULT_PREFERENCES_NAME = "exercise_progress"
        const val COMPLETED_EXERCISE_IDS_KEY = "completed_exercise_ids"
    }
}
