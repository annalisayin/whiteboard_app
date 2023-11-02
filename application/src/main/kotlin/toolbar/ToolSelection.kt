package toolbar
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import data.Shape
import data.TextBox

@Composable
fun ToolSelection(
    useSketch: MutableState<Boolean>,
    inUsedColor: MutableState<Color>,
    brushSize: MutableState<Int>,
    textSelected: MutableState<Boolean>,
    currentText: MutableState<String>,
    rectangleSelected: MutableState<Boolean>,
    circleSelected: MutableState<Boolean>,
    triangleSelected: MutableState<Boolean>,
) {
    MaterialTheme {
        Text("TOOL BAR")
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Shapes_tool(rectangleSelected, circleSelected, triangleSelected)
            Brush_tool(inUsedColor, brushSize, useSketch)
            Text_tool(textSelected, currentText)
            Selection_tool()
            Image_tool()
        }
    }
}
