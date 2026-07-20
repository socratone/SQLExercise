package io.github.socratone.sqlexercise.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.socratone.sqlexercise.R
import io.github.socratone.sqlexercise.ui.theme.SQLExerciseTheme

private val SubmissionResultSaver = Saver<SubmissionResult, String>(
    save = { result ->
        when (result) {
            SubmissionResult.NotSubmitted -> "not-submitted"
            SubmissionResult.Correct -> "correct"
            SubmissionResult.Incorrect -> "incorrect"
            is SubmissionResult.QueryError -> "query-error:${result.message}"
        }
    },
    restore = { saved ->
        when (saved) {
            "not-submitted" -> SubmissionResult.NotSubmitted
            "correct" -> SubmissionResult.Correct
            "incorrect" -> SubmissionResult.Incorrect
            else -> SubmissionResult.QueryError(saved.removePrefix("query-error:"))
        }
    },
)

/** 상세 화면의 공통 상단 바와 로딩, 성공, 실패 상태를 분기합니다. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LevelDetailRoute(
    uiState: LevelDetailUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val exercise = (uiState as? LevelDetailUiState.Content)?.exercise

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(exercise?.levelTitle.orEmpty()) },
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
            is LevelDetailUiState.Content -> LevelDetailContent(
                exercise = uiState.exercise,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

/**
 * 사용자가 작성한 SQL과 채점 결과를 보관하는 상태 보유 영역입니다.
 * 실제 API 연동 후에는 이 상태와 이벤트 처리를 ViewModel로 이동할 수 있습니다.
 */
@Composable
private fun LevelDetailContent(
    exercise: LevelExercise,
    modifier: Modifier = Modifier,
) {
    // 화면 회전이나 프로세스 복원 시에도 현재 문제의 입력 내용을 유지합니다.
    var sqlInput by rememberSaveable(exercise.id) { mutableStateOf("") }
    var submissionResult by rememberSaveable(exercise.id, stateSaver = SubmissionResultSaver) {
        mutableStateOf(SubmissionResult.NotSubmitted)
    }

    LevelDetailScreen(
        exercise = exercise,
        sqlInput = sqlInput,
        submissionResult = submissionResult,
        onInputChange = { updatedSql ->
            sqlInput = updatedSql

            // 채점 후 SQL이 바뀌면 이전 결과는 더 이상 유효하지 않습니다.
            submissionResult = SubmissionResult.NotSubmitted
        },
        onReset = {
            sqlInput = ""
            submissionResult = SubmissionResult.NotSubmitted
        },
        onSubmit = {
            submissionResult = evaluateSqlAnswer(
                input = sqlInput,
                expected = exercise.expectedSql,
                orderSensitive = exercise.orderSensitive,
            )
        },
        modifier = modifier,
    )
}

/**
 * 전달받은 데이터와 이벤트만 사용해 문제, 입력창, 동작 버튼을 그리는 상태 없는 UI입니다.
 * 이 구조 덕분에 데이터 출처가 로컬에서 API로 바뀌어도 화면 코드를 재사용할 수 있습니다.
 */
@Composable
fun LevelDetailScreen(
    exercise: LevelExercise,
    sqlInput: String,
    submissionResult: SubmissionResult,
    onInputChange: (String) -> Unit,
    onReset: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            // 키보드가 버튼과 피드백을 가리지 않도록 키보드 높이만큼 여백을 확보합니다.
            .imePadding()
            // 작은 화면이나 키보드가 열린 상태에서도 전체 콘텐츠에 접근할 수 있게 합니다.
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.question),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = exercise.question,
            style = MaterialTheme.typography.bodyLarge,
        )
        OutlinedTextField(
            value = sqlInput,
            onValueChange = onInputChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.sql_input_label)) },
            minLines = 8,
            maxLines = 12,
            textStyle = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily.Monospace),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedButton(
                onClick = onReset,
                modifier = Modifier.weight(1f),
            ) {
                Text(stringResource(R.string.reset))
            }
            Button(
                onClick = onSubmit,
                modifier = Modifier.weight(1f),
                enabled = sqlInput.isNotBlank(),
            ) {
                Text(stringResource(R.string.submit))
            }
        }
        SubmissionFeedback(submissionResult)
    }
}

/** 제출 전에는 아무것도 표시하지 않고, 제출 후에만 정답 여부를 보여줍니다. */
@Composable
private fun SubmissionFeedback(result: SubmissionResult) {
    val message: String
    val color: Color
    when (result) {
        SubmissionResult.NotSubmitted -> return
        SubmissionResult.Correct -> {
            message = stringResource(R.string.correct_answer)
            color = MaterialTheme.colorScheme.primary
        }
        SubmissionResult.Incorrect -> {
            message = stringResource(R.string.incorrect_answer)
            color = MaterialTheme.colorScheme.error
        }
        is SubmissionResult.QueryError -> {
            message = result.message
            color = MaterialTheme.colorScheme.error
        }
    }
    Text(text = message, color = color, style = MaterialTheme.typography.bodyLarge)
}

@Preview(showBackground = true)
@Composable
private fun LevelDetailPreview() {
    SQLExerciseTheme {
        LevelDetailScreen(
            exercise = sampleExercises.first(),
            sqlInput = "SELECT * FROM users;",
            submissionResult = SubmissionResult.Correct,
            onInputChange = {},
            onReset = {},
            onSubmit = {},
        )
    }
}
