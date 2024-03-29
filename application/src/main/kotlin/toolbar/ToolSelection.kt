package toolbar

import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

@Composable
fun ToolSelection(
    currentTool: MutableState<Int>,
    inUsedColor: MutableState<Int>,
    brushSize: MutableState<Int>,
//    useSketch: MutableState<Boolean>,
//    textSelected: MutableState<Boolean>,
    currentText: MutableState<String>,
//    rectangleSelected: MutableState<Boolean>,
//    circleSelected: MutableState<Boolean>,
//    triangleSelected: MutableState<Boolean>,
    inDelete: MutableState<Boolean>,
    inClearAll: MutableState<Boolean>,
) {
    val focusManager = LocalFocusManager.current
    MaterialTheme {
        Text("TOOL BAR")
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                .height(300.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            User_Login()
            Color_Size(inUsedColor, brushSize)
            Shapes_tool(currentTool)
            Brush_tool(inUsedColor, brushSize, currentTool)
            Text_tool(currentText, currentTool)
            Delete_tool(inDelete, currentTool)
            Clear_all_tool(inClearAll, currentTool)
        }
    }
}
