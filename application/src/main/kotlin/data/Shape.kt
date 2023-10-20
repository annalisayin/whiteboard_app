package data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

enum class ShapeType {
    Rectangle,
    Circle,
    Triangle
}

data class Shape(
    val type: ShapeType,
    val color: Color,
    val rect: Rectangle? = null,
    val circle: Circle? = null,
    val triangle: Triangle? = null,
    val position: MutableState<Offset> = mutableStateOf(Offset(0f, 0f))
)