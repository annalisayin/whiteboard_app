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
    shapesOnCanvas: MutableList<Shape>,
    isInTextMode: MutableState<Boolean>,
    currentText: MutableState<String>
) {
    MaterialTheme {
        Text("TOOL BAR")
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Brush_tool(inUsedColor, brushSize, useSketch)
            Shapes_tool { shape ->
                shapesOnCanvas.add(shape) // Add the shape to the canvas
            }
            Text_tool(isInTextMode, currentText)
            Selection_tool()
            Image_tool()
        }
    }
}
