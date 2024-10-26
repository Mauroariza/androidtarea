package com.example.aplicacion.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

@Composable
fun AplicacionTheme(
    useDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) {
        darkColorScheme(
            primary = DarkPrimary,
            secondary = DarkSecondary,
            background = DarkBackground,
            surface = DarkSurface,
            error = DarkError
        )
    } else {
        lightColorScheme(
            primary = Purple40,
            secondary = PurpleGrey40,
            tertiary = Pink40
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}