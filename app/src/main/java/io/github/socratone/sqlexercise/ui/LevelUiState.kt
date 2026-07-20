package io.github.socratone.sqlexercise.ui

/** 레벨 목록에서 화면에 노출할 최소 정보입니다. */
data class LevelSummary(
    val id: Int,
    val title: String,
    val stage: ExerciseStage = ExerciseStage.fromExerciseId(id),
    val isCompleted: Boolean = false,
)

enum class ExerciseStage(
    val title: String,
    val learningGoal: String,
) {
    BasicQuery("1단계 · 기본 조회", "SELECT, 열 선택, 별칭, 중복 제거"),
    Filtering("2단계 · 조건과 정렬", "WHERE, NULL, LIKE, IN, BETWEEN, ORDER BY"),
    Expressions("3단계 · 표현식과 함수", "문자열, 숫자, 날짜, CASE, COALESCE"),
    Aggregation("4단계 · 집계와 그룹화", "집계 함수, GROUP BY, HAVING, 조건부 집계"),
    Joins("5단계 · 조인", "내부 조인, 외부 조인, 다중 조인, 셀프 조인"),
    Subqueries("6단계 · 서브쿼리와 집합 연산", "서브쿼리, EXISTS, UNION, INTERSECT, EXCEPT"),
    Windows("7단계 · CTE와 윈도 함수", "WITH, 순위, 누적 집계, 이전 행 비교"),
    Advanced("8단계 · 고급 분석", "재귀 CTE, 다단계 집계, 관계 및 기간 분석"),
    ;

    companion object {
        fun fromExerciseId(id: Int): ExerciseStage = when (id) {
            in 1..6 -> BasicQuery
            in 7..16 -> Filtering
            in 17..24 -> Expressions
            in 25..32 -> Aggregation
            in 33..42 -> Joins
            in 43..50 -> Subqueries
            in 51..58 -> Windows
            in 59..70 -> Advanced
            else -> error("Unknown exercise id: $id")
        }
    }
}

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
    val stage: ExerciseStage = ExerciseStage.fromExerciseId(id),
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
