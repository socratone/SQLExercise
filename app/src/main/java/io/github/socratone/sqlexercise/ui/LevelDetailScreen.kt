package io.github.socratone.sqlexercise.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.TextFieldValue
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

internal val sqlKeywordTokens = listOf(
    "SELECT",
    "DISTINCT",
    "FROM",
    "WHERE",
    "JOIN",
    "LEFT JOIN",
    "GROUP BY",
    "HAVING",
    "ORDER BY",
    "ASC",
    "DESC",
    "LIMIT",
)

internal val sqlTableTokens = listOf(
    "regions",
    "countries",
    "locations",
    "departments",
    "jobs",
    "employees",
    "job_history",
)

private val sqlSymbolTokens = listOf("*", "_")

/** 선택 영역을 SQL 토큰으로 바꾸고 주변 문맥에 필요한 공백만 추가합니다. */
internal fun insertSqlToken(value: TextFieldValue, token: String): TextFieldValue {
    val selectionStart = value.selection.min
    val selectionEnd = value.selection.max
    val before = value.text.substring(0, selectionStart)
    val after = value.text.substring(selectionEnd)
    val needsLeadingSpace = before.lastOrNull()?.let { previous ->
        !previous.isWhitespace() && previous !in "(."
    } ?: false
    val needsTrailingSpace = after.firstOrNull()?.let { next ->
        !next.isWhitespace() && next !in "),;."
    } ?: true
    val insertedText = buildString {
        if (needsLeadingSpace) append(' ')
        append(token)
        if (needsTrailingSpace) append(' ')
    }
    val updatedText = before + insertedText + after
    val cursorPosition = before.length + insertedText.length

    return value.copy(
        text = updatedText,
        selection = TextRange(cursorPosition),
        composition = null,
    )
}

/** 선택 영역을 공백 보정 없이 특수문자로 바꿉니다. */
internal fun insertSqlSymbol(value: TextFieldValue, symbol: String): TextFieldValue {
    val selectionStart = value.selection.min
    val selectionEnd = value.selection.max
    val updatedText = value.text.replaceRange(selectionStart, selectionEnd, symbol)

    return value.copy(
        text = updatedText,
        selection = TextRange(selectionStart + symbol.length),
        composition = null,
    )
}

/** 상세 화면의 로딩, 성공, 실패 상태를 분기합니다. */
@Composable
fun LevelDetailRoute(
    uiState: LevelDetailUiState,
    onBackClick: () -> Unit,
    onExerciseCompleted: (Int) -> Unit = {},
    previousEnabled: Boolean = false,
    nextEnabled: Boolean = false,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val exercise = (uiState as? LevelDetailUiState.Content)?.exercise

    when (uiState) {
        LevelDetailUiState.Loading -> DetailStateContent(
            title = exercise?.levelTitle.orEmpty(),
            onBackClick = onBackClick,
            modifier = modifier,
        ) { contentModifier -> LoadingContent(contentModifier) }
        LevelDetailUiState.Error -> DetailStateContent(
            title = exercise?.levelTitle.orEmpty(),
            onBackClick = onBackClick,
            modifier = modifier,
        ) { contentModifier ->
            MessageContent(
                message = stringResource(R.string.load_failed),
                modifier = contentModifier,
            )
        }
        is LevelDetailUiState.Content -> LevelDetailContent(
            exercise = uiState.exercise,
            onBackClick = onBackClick,
            onExerciseCompleted = onExerciseCompleted,
            previousEnabled = previousEnabled,
            nextEnabled = nextEnabled,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun DetailStateContent(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding(),
    ) {
        DetailHeader(title = title, onBackClick = onBackClick)
        content(Modifier.weight(1f))
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun DetailHeader(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back),
                )
            }
        },
    )
}

/**
 * 사용자가 작성한 SQL과 채점 결과를 보관하는 상태 보유 영역입니다.
 * 실제 API 연동 후에는 이 상태와 이벤트 처리를 ViewModel로 이동할 수 있습니다.
 */
@Composable
private fun LevelDetailContent(
    exercise: LevelExercise,
    onBackClick: () -> Unit,
    onExerciseCompleted: (Int) -> Unit,
    previousEnabled: Boolean,
    nextEnabled: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // 화면 회전이나 프로세스 복원 시에도 현재 문제의 입력 내용을 유지합니다.
    var sqlInput by rememberSaveable(exercise.id, stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var submissionResult by rememberSaveable(exercise.id, stateSaver = SubmissionResultSaver) {
        mutableStateOf(SubmissionResult.NotSubmitted)
    }
    var schemaExpanded by rememberSaveable(exercise.id) { mutableStateOf(false) }

    LevelDetailScreen(
        exercise = exercise,
        onBackClick = onBackClick,
        sqlInput = sqlInput,
        submissionResult = submissionResult,
        onInputChange = { updatedSql ->
            val textChanged = sqlInput.text != updatedSql.text
            sqlInput = updatedSql

            // 채점 후 SQL이 바뀌면 이전 결과는 더 이상 유효하지 않습니다.
            if (textChanged) {
                submissionResult = SubmissionResult.NotSubmitted
            }
        },
        onReset = {
            sqlInput = TextFieldValue()
            submissionResult = SubmissionResult.NotSubmitted
        },
        onSubmit = {
            val result = evaluateSqlAnswer(
                input = sqlInput.text,
                expected = exercise.expectedSql,
                orderSensitive = exercise.orderSensitive,
            )
            submissionResult = result
            if (result == SubmissionResult.Correct) {
                onExerciseCompleted(exercise.id)
            }
        },
        schemaExpanded = schemaExpanded,
        onSchemaToggle = { schemaExpanded = !schemaExpanded },
        previousEnabled = previousEnabled,
        nextEnabled = nextEnabled,
        onPreviousClick = onPreviousClick,
        onNextClick = onNextClick,
        modifier = modifier,
    )
}

/**
 * 전달받은 데이터와 이벤트만 사용해 문제, 입력창, 동작 버튼을 그리는 상태 없는 UI입니다.
 * 이 구조 덕분에 데이터 출처가 로컬에서 API로 바뀌어도 화면 코드를 재사용할 수 있습니다.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LevelDetailScreen(
    exercise: LevelExercise,
    sqlInput: TextFieldValue,
    submissionResult: SubmissionResult,
    onInputChange: (TextFieldValue) -> Unit,
    onReset: () -> Unit,
    onSubmit: () -> Unit,
    previousEnabled: Boolean = false,
    nextEnabled: Boolean = false,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    schemaExpanded: Boolean = false,
    onSchemaToggle: () -> Unit = {},
    onBackClick: () -> Unit = {},
) {
    val sqlInputFocusRequester = remember { FocusRequester() }
    val contentScrollState = rememberScrollState()
    val isImeVisible = WindowInsets.isImeVisible
    var showTableTokens by rememberSaveable(exercise.id) { mutableStateOf(false) }

    LaunchedEffect(exercise.id, isImeVisible) {
        if (!isImeVisible) {
            contentScrollState.scrollTo(0)
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .imePadding(),
        ) {
            DetailHeader(
                title = exercise.levelTitle,
                onBackClick = onBackClick,
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(contentScrollState)
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.question),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        onClick = onPreviousClick,
                        enabled = previousEnabled,
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.previous_exercise),
                        )
                    }
                    IconButton(
                        onClick = onNextClick,
                        enabled = nextEnabled,
                        modifier = Modifier.size(40.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_forward),
                            contentDescription = stringResource(R.string.next_exercise),
                        )
                    }
                }
                Text(
                    text = exercise.question,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            OutlinedButton(
                onClick = onSchemaToggle,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    stringResource(
                        if (schemaExpanded) R.string.hide_schema else R.string.show_schema,
                    ),
                )
            }
            if (schemaExpanded) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.database_schema),
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = hrSchemaReference,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = FontFamily.Monospace,
                            ),
                        )
                    }
                }
            }
            OutlinedTextField(
                value = sqlInput,
                onValueChange = onInputChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(sqlInputFocusRequester),
                label = { Text(stringResource(R.string.sql_input_label)) },
                minLines = 6,
                maxLines = 10,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    fontFamily = FontFamily.Monospace,
                ),
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
                    enabled = sqlInput.text.isNotBlank(),
                ) {
                    Text(stringResource(R.string.submit))
                }
            }
                SubmissionFeedback(submissionResult)
            }

            if (isImeVisible) {
                SqlInputAccessoryBar(
                    showTableTokens = showTableTokens,
                    onCategorySelected = { showTables -> showTableTokens = showTables },
                    onTokenClick = { token ->
                        onInputChange(insertSqlToken(sqlInput, token))
                        sqlInputFocusRequester.requestFocus()
                    },
                    onSymbolClick = { symbol ->
                        onInputChange(insertSqlSymbol(sqlInput, symbol))
                        sqlInputFocusRequester.requestFocus()
                    },
                )
            }
        }
    }
}

@Composable
internal fun SqlInputAccessoryBar(
    showTableTokens: Boolean,
    onCategorySelected: (Boolean) -> Unit,
    onTokenClick: (String) -> Unit,
    onSymbolClick: (String) -> Unit = onTokenClick,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 3.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = !showTableTokens,
                    onClick = { onCategorySelected(false) },
                    label = { Text(stringResource(R.string.sql_keywords)) },
                )
                FilterChip(
                    selected = showTableTokens,
                    onClick = { onCategorySelected(true) },
                    label = { Text(stringResource(R.string.sql_tables)) },
                )
                sqlSymbolTokens.forEach { symbol ->
                    AssistChip(
                        onClick = { onSymbolClick(symbol) },
                        label = {
                            Text(
                                text = symbol,
                                fontFamily = FontFamily.Monospace,
                            )
                        },
                    )
                }
            }
            SqlTokenChips(
                tokens = if (showTableTokens) sqlTableTokens else sqlKeywordTokens,
                onTokenClick = onTokenClick,
            )
        }
    }
}

@Composable
private fun SqlTokenChips(
    tokens: List<String>,
    onTokenClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        tokens.forEach { token ->
            AssistChip(
                onClick = { onTokenClick(token) },
                label = {
                    Text(
                        text = token,
                        fontFamily = FontFamily.Monospace,
                    )
                },
            )
        }
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
            sqlInput = TextFieldValue("SELECT * FROM employees;"),
            submissionResult = SubmissionResult.Correct,
            onInputChange = {},
            onReset = {},
            onSubmit = {},
        )
    }
}
