package com.sarinvoice.app

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val LocalDark = staticCompositionLocalOf { true }

// Dark Mode ON = Black & Blue Neon
private val NeonScheme = darkColorScheme(
    primary = Color(0xFF00B8FF), onPrimary = Color.Black,
    secondary = Color(0xFF3D7BFF), onSecondary = Color.White,
    tertiary = Color(0xFF00E5FF),
    background = Color.Black, onBackground = Color(0xFFE6F6FF),
    surface = Color(0xFF05080F), onSurface = Color(0xFFE6F6FF),
    surfaceVariant = Color(0xFF0A1428), onSurfaceVariant = Color(0xFF8FD8FF),
    primaryContainer = Color(0xFF00243D), onPrimaryContainer = Color(0xFF9BE3FF),
    secondaryContainer = Color(0xFF0B2A52), onSecondaryContainer = Color(0xFFBFE4FF),
    outline = Color(0xFF00B8FF), outlineVariant = Color(0xFF0E3A5C),
    error = Color(0xFFFF5C8A), onError = Color.Black
)

private val LightScheme = lightColorScheme(
    primary = Color(0xFF1565C0), onPrimary = Color.White,
    background = Color(0xFFF6F8FB), onBackground = Color(0xFF14202E),
    surface = Color.White, onSurface = Color(0xFF14202E),
    surfaceVariant = Color(0xFFEAF0F8), onSurfaceVariant = Color(0xFF3F4F63),
    primaryContainer = Color(0xFFD6E6FA), onPrimaryContainer = Color(0xFF0B3A73),
    secondaryContainer = Color(0xFFDCE6F3), onSecondaryContainer = Color(0xFF14202E),
    error = Color(0xFFB3261E)
)

@Composable
fun SarTheme(dark: Boolean, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalDark provides dark) {
        MaterialTheme(colorScheme = if (dark) NeonScheme else LightScheme, content = content)
    }
}

@Composable
fun Panel(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    val border = if (LocalDark.current) BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)) else null
    Card(
        modifier = modifier.fillMaxWidth().then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = border
    ) { Column(Modifier.padding(16.dp), content = content) }
}
