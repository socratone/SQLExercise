package io.github.socratone.sqlexercise.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.socratone.sqlexercise.R
import io.github.socratone.sqlexercise.ui.theme.SQLExerciseTheme
import io.github.socratone.sqlexercise.ui.theme.Success

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelListRoute(
    uiState: LevelListUiState,
    onLevelClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
    ) { innerPadding ->
        when (uiState) {
            LevelListUiState.Loading -> LoadingContent(Modifier.padding(innerPadding))
            LevelListUiState.Error -> MessageContent(
                message = stringResource(R.string.load_failed),
                modifier = Modifier.padding(innerPadding),
            )
            is LevelListUiState.Content -> LevelListScreen(
                levels = uiState.levels,
                onLevelClick = onLevelClick,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Composable
fun LevelListScreen(
    levels: List<LevelSummary>,
    onLevelClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (levels.isEmpty()) {
        MessageContent(
            message = stringResource(R.string.empty_levels),
            modifier = modifier,
        )
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        levels.groupBy(LevelSummary::stage).forEach { (stage, stageLevels) ->
            item(key = "stage-${stage.name}") {
                androidx.compose.foundation.layout.Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(top = 8.dp),
                ) {
                    Text(stage.title, style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
                    Text(
                        stage.learningGoal,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            items(items = stageLevels, key = LevelSummary::id) { level ->
                val completionState = stringResource(
                    if (level.isCompleted) R.string.completed else R.string.not_completed,
                )
                val buttonColor = if (level.isCompleted) Success else MaterialTheme.colorScheme.primary
                val borderColor = if (level.isCompleted) Success else MaterialTheme.colorScheme.outline

                OutlinedButton(
                    onClick = { onLevelClick(level.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { stateDescription = completionState },
                    contentPadding = PaddingValues(vertical = 16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = buttonColor),
                    border = BorderStroke(1.dp, borderColor),
                ) {
                    Text(level.title)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LevelListPreview() {
    SQLExerciseTheme {
        LevelListRoute(
            uiState = LevelListUiState.Content(
                (1..10).map {
                    LevelSummary(it, "$it level", ExerciseStage.fromExerciseId(it))
                },
            ),
            onLevelClick = {},
        )
    }
}
