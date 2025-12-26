package com.example.memoria.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = MemoriaDarkPrimary,
    onPrimary = MemoriaDarkOnPrimary,
    primaryContainer = MemoriaDarkPrimaryContainer,
    onPrimaryContainer = MemoriaDarkOnPrimaryContainer,
    secondary = MemoriaDarkSecondary,
    onSecondary = MemoriaDarkOnSecondary,
    secondaryContainer = MemoriaDarkSecondaryContainer,
    onSecondaryContainer = MemoriaDarkOnSecondaryContainer,
    tertiary = MemoriaDarkTertiary,
    onTertiary = MemoriaDarkOnTertiary,
    tertiaryContainer = MemoriaDarkTertiaryContainer,
    onTertiaryContainer = MemoriaDarkOnTertiaryContainer,
    error = MemoriaDarkError,
    onError = MemoriaDarkOnError,
    errorContainer = MemoriaDarkErrorContainer,
    onErrorContainer = MemoriaDarkOnErrorContainer,
    background = MemoriaDarkBackground,
    onBackground = MemoriaDarkOnBackground,
    surface = MemoriaDarkSurface,
    onSurface = MemoriaDarkOnSurface,
    surfaceVariant = MemoriaDarkSurfaceVariant,
    onSurfaceVariant = MemoriaDarkOnSurfaceVariant,
    outline = MemoriaDarkOutline,
    outlineVariant = MemoriaDarkOutlineVariant
)

private val LightColorScheme = lightColorScheme(
    primary = MemoriaLightPrimary,
    onPrimary = MemoriaLightOnPrimary,
    primaryContainer = MemoriaLightPrimaryContainer,
    onPrimaryContainer = MemoriaLightOnPrimaryContainer,
    secondary = MemoriaLightSecondary,
    onSecondary = MemoriaLightOnSecondary,
    secondaryContainer = MemoriaLightSecondaryContainer,
    onSecondaryContainer = MemoriaLightOnSecondaryContainer,
    tertiary = MemoriaLightTertiary,
    onTertiary = MemoriaLightOnTertiary,
    tertiaryContainer = MemoriaLightTertiaryContainer,
    onTertiaryContainer = MemoriaLightOnTertiaryContainer,
    error = MemoriaLightError,
    onError = MemoriaLightOnError,
    errorContainer = MemoriaLightErrorContainer,
    onErrorContainer = MemoriaLightOnErrorContainer,
    background = MemoriaLightBackground,
    onBackground = MemoriaLightOnBackground,
    surface = MemoriaLightSurface,
    onSurface = MemoriaLightOnSurface,
    surfaceVariant = MemoriaLightSurfaceVariant,
    onSurfaceVariant = MemoriaLightOnSurfaceVariant,
    outline = MemoriaLightOutline,
    outlineVariant = MemoriaLightOutlineVariant
)

@Composable
fun MemoriaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MemoriaTypography,
        shapes = MemoriaShapes,
        content = content
    )
}


