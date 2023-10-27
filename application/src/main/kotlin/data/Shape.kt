package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

abstract class Shape(var offset: Offset) {
    @Composable
    abstract fun draw()
    val color: Color = Color.Black
}