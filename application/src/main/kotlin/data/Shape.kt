package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

abstract class Shape(var offset: Offset, var color: Color, var size: Dp) {
    @Composable
    abstract fun draw()
    //val color: Color = Color.Black
}