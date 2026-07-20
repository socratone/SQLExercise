package io.github.socratone.sqlexercise.ui

/** 레벨 목록에서 화면에 노출할 최소 정보입니다. */
data class LevelSummary(
    val id: Int,
    val title: String,
)

/**
 * 상세 화면에서 사용하는 문제 데이터입니다.
 * 현재는 로컬 샘플을 사용하지만, 추후 API 응답을 이 형태로 변환해 전달할 수 있습니다.
 */
data class LevelExercise(
    val id: Int,
    val levelTitle: String,
    val question: String,
    val expectedSql: String,
    val orderSensitive: Boolean = false,
)

/** 아직 제출하지 않은 상태를 포함한 SQL 채점 결과입니다. */
sealed interface SubmissionResult {
    data object NotSubmitted : SubmissionResult
    data object Correct : SubmissionResult
    data object Incorrect : SubmissionResult
    data class QueryError(val message: String) : SubmissionResult
}

/** 레벨 목록의 로딩, 성공, 실패 상태를 한 타입으로 관리합니다. */
sealed interface LevelListUiState {
    data object Loading : LevelListUiState
    data class Content(val levels: List<LevelSummary>) : LevelListUiState
    data object Error : LevelListUiState
}

/** 문제 상세의 로딩, 성공, 실패 상태를 한 타입으로 관리합니다. */
sealed interface LevelDetailUiState {
    data object Loading : LevelDetailUiState
    data class Content(val exercise: LevelExercise) : LevelDetailUiState
    data object Error : LevelDetailUiState
}
