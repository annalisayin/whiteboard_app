package data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import kotlinx.serialization.Serializable

@Serializable
abstract class Shape {
    abstract var x: Float
    abstract var y: Float
    abstract var color: Int
    abstract var size: Int
    @Composable
    abstract fun draw()
    //val color: Color = Color.Black
}