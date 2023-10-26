package data

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

class Rectangle() : Shape() {
    override var x: Double = 0.0
    override var y: Double = 0.0
    override var w: Double = 0.0
    override var h: Double = 0.0
    @Composable
    override fun  onDraw() { RectangleComposable(color = color) }
}

@Composable
fun RectangleComposable(color: Color){
    Column(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.Center)) {
        Box(
            modifier = Modifier.size(100.dp).clip(RectangleShape).background(color)
        )
    }
}
