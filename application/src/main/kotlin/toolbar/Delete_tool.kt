package toolbar


import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Delete_tool(currentTool: MutableState<Int>,) {
    val color = if (currentTool.value == 5) Color.Green else Color.Gray
    Button(
        modifier = Modifier
            .padding(30.dp),
        onClick = {
            currentTool.value = if (currentTool.value == 5) -1 else 5
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = color)
    ) {
        Icon(Icons.Filled.Delete, contentDescription = "Localized description")
    }
}