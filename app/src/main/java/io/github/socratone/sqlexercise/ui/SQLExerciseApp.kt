package io.github.socratone.sqlexercise.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.socratone.sqlexercise.data.ExerciseProgressStore

private const val LEVEL_LIST_ROUTE = "level-list"
private const val LEVEL_DETAIL_ROUTE = "level-detail/{levelId}"

@Composable
fun SQLExerciseApp() {
    val navController = rememberNavController()
    val applicationContext = LocalContext.current.applicationContext
    val progressStore = remember(applicationContext) {
        ExerciseProgressStore(applicationContext)
    }
    var completedExerciseIds by remember {
        mutableStateOf(progressStore.loadCompletedExerciseIds())
    }

    // 현재는 로컬 데이터를 사용하며, API 연동 시 ViewModel이 제공하는 상태로 대체합니다.
    val exercises = remember { sampleExercises }

    // 목록 화면에는 정답을 노출할 필요가 없으므로 제목과 ID만 추려서 전달합니다.
    val levels = remember(exercises, completedExerciseIds) {
        exercises.map { exercise ->
            LevelSummary(
                id = exercise.id,
                title = exercise.levelTitle,
                stage = exercise.stage,
                isCompleted = exercise.id in completedExerciseIds,
            )
        }
    }

    NavHost(
        navController = navController,
        startDestination = LEVEL_LIST_ROUTE,
    ) {
        composable(LEVEL_LIST_ROUTE) {
            LevelListRoute(
                uiState = LevelListUiState.Content(levels),
                onLevelClick = { levelId ->
                    navController.navigate("level-detail/$levelId")
                },
            )
        }
        composable(
            route = LEVEL_DETAIL_ROUTE,
            arguments = listOf(navArgument("levelId") { type = NavType.IntType }),
        ) { backStackEntry ->
            // 목록에서 전달받은 ID에 해당하는 문제를 찾아 상세 화면 상태를 만듭니다.
            val levelId = backStackEntry.arguments?.getInt("levelId")
            val selectedIndex = exercises.indexOfFirst { it.id == levelId }
            val selectedExercise = exercises.getOrNull(selectedIndex)
            val detailState = selectedExercise?.let(LevelDetailUiState::Content)
                ?: LevelDetailUiState.Error
            val previousExerciseId = exercises.getOrNull(selectedIndex - 1)?.id
            val nextExerciseId = exercises.getOrNull(selectedIndex + 1)?.id

            fun navigateToExercise(exerciseId: Int) {
                navController.navigate("level-detail/$exerciseId") {
                    // 상세 화면 이력을 쌓지 않아 상단 Back이 항상 목록으로 돌아가게 합니다.
                    popUpTo(LEVEL_LIST_ROUTE) { inclusive = false }
                    launchSingleTop = true
                }
            }

            LevelDetailRoute(
                uiState = detailState,
                onBackClick = navController::navigateUp,
                previousEnabled = previousExerciseId != null,
                nextEnabled = nextExerciseId != null,
                onPreviousClick = {
                    previousExerciseId?.let(::navigateToExercise)
                },
                onNextClick = {
                    nextExerciseId?.let(::navigateToExercise)
                },
                onExerciseCompleted = { completedId ->
                    if (completedId !in completedExerciseIds) {
                        progressStore.markCompleted(completedId)
                        completedExerciseIds = completedExerciseIds + completedId
                    }
                },
            )
        }
    }
}
