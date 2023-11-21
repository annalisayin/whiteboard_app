package toolbar

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Brush_tool(inUsedColor: MutableState<Int>, brushSize: MutableState<Int>, currentTool: MutableState<Int>) {
    val color = if (currentTool.value == 0) Color.Green else Color.Gray
    Button(
            modifier = Modifier
                .padding(30.dp)
                .height(50.dp),
            onClick = {
                //openDialog.value = !openDialog.value
                currentTool.value = if (currentTool.value == 0) -1 else 0
            },
        colors = ButtonDefaults.buttonColors(backgroundColor = color)
    ) {
            Icon(Icons.Filled.Edit, contentDescription = "Localized description")
        }
    }
