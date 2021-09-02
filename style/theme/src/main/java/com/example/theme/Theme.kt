package com.example.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.color.*
import com.example.shape.Shapes
import com.example.typography.Typography
import kotlinx.coroutines.flow.Flow

private val DarkColorPalette = darkColors(
    primary = primary,
    primaryVariant = primaryVariant,
    secondary = secondary,
    secondaryVariant = secondaryVariant
)

private val LightColorPalette = lightColors(
    primary = primary,
    primaryVariant = primaryVariant,
    secondary = secondary,
    surface = surface,
    secondaryVariant = secondaryVariant
)



@Composable
fun BooksTheme(darkThemeFlow: Flow<Boolean>, defaultValue: Boolean, content: @Composable ()-> Unit){

    val isSystemInDark = darkThemeFlow.collectAsState(initial = defaultValue).value

    val colors = if(isSystemInDark){
        DarkColorPalette
    }else{
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}