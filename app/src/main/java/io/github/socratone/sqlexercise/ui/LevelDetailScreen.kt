package io.github.socratone.sqlexercise.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.socratone.sqlexercise.R
import io.github.socratone.sqlexercise.ui.theme.SQLExerciseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelDetailRoute(
    uiState: LevelDetailUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = (uiState as? LevelDetailUiState.Content)?.level?.title.orEmpty()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text(stringResource(R.string.back))
                    }
                },
            )
        },
    ) { innerPadding ->
        when (uiState) {
            LevelDetailUiState.Loading -> LoadingContent(Modifier.padding(innerPadding))
            LevelDetailUiState.Error -> MessageContent(
                message = stringResource(R.string.load_failed),
                modifier = Modifier.padding(innerPadding),
            )
            is LevelDetailUiState.Content -> MessageContent(
                message = stringResource(R.string.content_not_ready),
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LevelDetailPreview() {
    SQLExerciseTheme {
        LevelDetailRoute(
            uiState = LevelDetailUiState.Content(LevelSummary(1, "1 level")),
            onBackClick = {},
        )
    }
}
