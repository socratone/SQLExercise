package io.github.socratone.sqlexercise.ui

data class LevelSummary(
    val id: Int,
    val title: String,
)

sealed interface LevelListUiState {
    data object Loading : LevelListUiState
    data class Content(val levels: List<LevelSummary>) : LevelListUiState
    data object Error : LevelListUiState
}

sealed interface LevelDetailUiState {
    data object Loading : LevelDetailUiState
    data class Content(val level: LevelSummary) : LevelDetailUiState
    data object Error : LevelDetailUiState
}
