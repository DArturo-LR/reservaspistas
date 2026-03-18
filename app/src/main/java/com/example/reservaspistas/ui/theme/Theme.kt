package com.example.reservaspistas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColoresApp = lightColorScheme(
    primary          = Azul,
    onPrimary        = Blanco,
    primaryContainer = AzulFondo,
    background       = GrisClaro,
    surface          = Blanco,
    onBackground     = Negro,
    onSurface        = Negro,
    error            = Rojo,
)

@Composable
fun ReservaspistasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColoresApp,
        typography  = Typography,
        content     = content
    )
}
