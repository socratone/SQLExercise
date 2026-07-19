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
    val levels = remember {
        (1..10).map { level ->
            LevelSummary(id = level, title = "$level level")
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
            val levelId = backStackEntry.arguments?.getInt("levelId")
            val selectedLevel = levels.firstOrNull { it.id == levelId }
            val detailState = selectedLevel?.let(LevelDetailUiState::Content)
                ?: LevelDetailUiState.Error

            LevelDetailRoute(
                uiState = detailState,
                onBackClick = navController::navigateUp,
            )
        }
    }
}
