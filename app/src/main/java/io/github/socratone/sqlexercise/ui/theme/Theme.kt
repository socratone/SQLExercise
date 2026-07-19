package io.github.socratone.sqlexercise.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = LightContent,
    onPrimary = DarkContent,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = LightContent,
    secondary = MutedContent,
    onSecondary = DarkContent,
    background = DarkBackground,
    onBackground = LightContent,
    surface = DarkSurface,
    onSurface = LightContent,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = MutedContent,
    outline = Outline,
    error = Error,
    onError = DarkContent,
)

@Composable
fun SQLExerciseTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content,
    )
}
