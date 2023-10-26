package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

abstract class Shape() {
    @Composable
    abstract fun onDraw()
    abstract val x: Double
    abstract val y: Double
    abstract val w: Double
    abstract val h: Double
    val color: Color = Color.Black
}