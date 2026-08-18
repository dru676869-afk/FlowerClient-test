package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = AccentWhite,
    onPrimary = AmoledBlack,
    primaryContainer = SurfaceDark,
    onPrimaryContainer = AccentWhite,
    secondary = DiscordBlurple,
    onSecondary = AccentWhite,
    secondaryContainer = SurfaceContainer,
    onSecondaryContainer = AccentWhite,
    tertiary = CyanAccent,
    onTertiary = AmoledBlack,
    background = AmoledBlack,
    onBackground = AccentWhite,
    surface = AmoledBlack,
    onSurface = AccentWhite,
    surfaceVariant = SurfaceContainer,
    onSurfaceVariant = MutedText,
    outline = BorderSubtle,
    outlineVariant = BorderMedium,
    error = AlertCoralRed,
    onError = AccentWhite
)

@Composable
fun FlowerClientTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = AmoledBlack.toArgb()
                window.navigationBarColor = AmoledBlack.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    FlowerClientTheme(content = content)
}
