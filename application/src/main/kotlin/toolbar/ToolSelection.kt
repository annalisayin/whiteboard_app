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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ToolSelection(
    currentTool: MutableState<Int>,
    inUsedColor: MutableState<Color>,
    brushSize: MutableState<Int>,
    currentText: MutableState<String>,
) {
    MaterialTheme {
        Text("TOOL BAR")
        //Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.height(300.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Color_Size(inUsedColor, brushSize)
            Brush_tool(inUsedColor, brushSize, currentTool)
            Shapes_tool(currentTool)
            Text_tool(currentTool, currentText)
            Undo_tool(currentTool)
        }
    }
}
