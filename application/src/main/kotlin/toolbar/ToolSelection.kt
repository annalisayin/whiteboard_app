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
import data.User

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
    val focusManager = LocalFocusManager.current
    MaterialTheme {
        Text("TOOL BAR")
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            User_Login()
            Color_Size(inUsedColor, brushSize)
            Shapes_tool(rectangleSelected, circleSelected, triangleSelected)
            Brush_tool(inUsedColor, brushSize, useSketch)
            Text_tool(textSelected, currentText)
            Selection_tool()
            Delete_tool(deleteObjects)
        }
    }
}
