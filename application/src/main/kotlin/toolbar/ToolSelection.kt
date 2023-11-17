package toolbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun ToolSelection(
    useSketch: MutableState<Boolean>,
    inUsedColor: MutableState<Int>,
    brushSize: MutableState<Int>,
    textSelected: MutableState<Boolean>,
    currentText: MutableState<String>,
    rectangleSelected: MutableState<Boolean>,
    circleSelected: MutableState<Boolean>,
    triangleSelected: MutableState<Boolean>,
    deleteObjects: MutableState<Boolean>
) {
    MaterialTheme {
        Text("TOOL BAR")
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Color_Size(inUsedColor, brushSize)
            Shapes_tool(rectangleSelected, circleSelected, triangleSelected)
            Brush_tool(inUsedColor, brushSize, useSketch)
            Text_tool(textSelected, currentText)
            Selection_tool()
            Delete_tool(deleteObjects)
        }
    }
}
