package toolbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
fun Color_Size(inUsedColor: MutableState<Color>, brushSize: MutableState<Int>) {
    Column(Modifier .padding(30.dp)) {
        ColorPicker(inUsedColor)
        BrushSizeSelector(brushSize)
    }
}