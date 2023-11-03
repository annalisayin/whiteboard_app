package canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Sketch(
    val start: Offset,
    val end: Offset,
    val color: Color = Color.Black,
    val width: Dp = 1.dp,
)