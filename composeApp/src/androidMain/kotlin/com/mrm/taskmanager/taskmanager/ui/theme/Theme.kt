package com.mrm.taskmanager.taskmanager.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Primary,
    onPrimary = SurfaceLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    error = ErrorRed,
)

@Composable
fun TaskManagerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors, typography = TaskTypography, content = content
    )
}