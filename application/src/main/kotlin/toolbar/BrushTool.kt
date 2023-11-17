package toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun Brush_tool(inUsedColor: MutableState<Color>, brushSize: MutableState<Int>, currentTool: MutableState<Int>) {
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
