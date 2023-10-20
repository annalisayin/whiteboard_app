package toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Slider
import androidx.compose.material.Text
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
fun BrushSizeSelector(brushSize: MutableState<Int>) {
    var sliderPosition by remember { mutableStateOf(0f) }
    val strokeWidth: List<Int> = (1 until 51).toList()

    Column(
        modifier = Modifier
            .width(200.dp)
    ) {
        Text("Pick a brush size from slider")
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                brushSize.value = strokeWidth[sliderPosition.toInt()]
            },
            valueRange = 0f..(strokeWidth.size - 1).toFloat(),
            steps = strokeWidth.size - 1,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(strokeWidth[sliderPosition.toInt()].dp)
                .background(Color.Black)
        )

        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Composable
fun ColorPicker(inUsedColor: MutableState<Color>) {
    var sliderPosition by remember { mutableStateOf(0f) }

    val colors: List<Color> = (0 until 100).map { index ->
        val colorInt = (index * 0xFFFFFF / 100) or 0xFF000000.toInt()
        Color(colorInt)
    }

    Column(
        modifier = Modifier
            .width(200.dp)
    ) {
        Text("Pick a color from slider")
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                inUsedColor.value = colors[sliderPosition.toInt()]
            },
            valueRange = 0f..(colors.size - 1).toFloat(),
            steps = colors.size - 1,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(colors[sliderPosition.toInt()])
        )

        Spacer(modifier = Modifier.height(15.dp))
    }
}

@Composable
fun Brush_tool(inUsedColor: MutableState<Color>, brushSize: MutableState<Int>) {
    val openDialog = remember { mutableStateOf(false) }
    val buttonTitle = remember {
        mutableStateOf("Show Pop Up")
    }
        Button(
            modifier = Modifier
                .padding(30.dp),
            onClick = {
                openDialog.value = !openDialog.value
            }
        ) {
            Icon(Icons.Filled.Edit, contentDescription = "Localized description")
        }
        Box {
            if (openDialog.value) {
                buttonTitle.value = "Hide Pop Up"
                Popup(
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties()
                ) {
                    Box(
                        Modifier
                            .padding(top = 5.dp)
                            .background(Color.Gray, RoundedCornerShape(10.dp))
                            .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
                    ) {
                        Column(Modifier .padding(all = 10.dp)) {
                            ColorPicker(inUsedColor)
                            BrushSizeSelector(brushSize)
                        }
                    }
                }
            }
        }
    }
