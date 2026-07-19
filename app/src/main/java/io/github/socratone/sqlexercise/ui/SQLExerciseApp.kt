package io.github.socratone.sqlexercise.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

private const val LEVEL_LIST_ROUTE = "level-list"
private const val LEVEL_DETAIL_ROUTE = "level-detail/{levelId}"

@Composable
fun SQLExerciseApp() {
    val navController = rememberNavController()

    // 현재는 로컬 데이터를 사용하며, API 연동 시 ViewModel이 제공하는 상태로 대체합니다.
    val exercises = remember { sampleExercises }

    // 목록 화면에는 정답을 노출할 필요가 없으므로 제목과 ID만 추려서 전달합니다.
    val levels = remember(exercises) {
        exercises.map { exercise ->
            LevelSummary(id = exercise.id, title = exercise.levelTitle)
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
            val selectedExercise = exercises.firstOrNull { it.id == levelId }
            val detailState = selectedExercise?.let(LevelDetailUiState::Content)
                ?: LevelDetailUiState.Error

            LevelDetailRoute(
                uiState = detailState,
                onBackClick = navController::navigateUp,
            )
        }
    }
}
