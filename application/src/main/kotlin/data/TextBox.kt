package data

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

//@Serializable
//class TextBox(var offsetX: Int, var offsetY: Int, var curtext: String, var color: Int, var size: Int) {
//    @Composable
//    fun draw(){
//        SimpleFilledTextField(curtext, offsetX, offsetY, color, size)
//    }
//}
@Serializable
data class TextBoxData(
    val offsetX: Int,
    val offsetY: Int,
    val curtext: String,
    val color: Int,
    val size: Int,
    val Id: Int? = -1,
)

class TextBox(
    val TextBoxData: TextBoxData,
    val Id: Int? = TextBoxData.Id,
    val inDelete: MutableState<Boolean>,
){
    @Composable
    fun draw() {
        SimpleFilledTextField(TextBoxData.curtext, TextBoxData.offsetX, TextBoxData.offsetY, TextBoxData.color, TextBoxData.size, Id, inDelete)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimpleFilledTextField(curtext: String, offsetX: Int, offsetY: Int, color: Int, size: Int, Id: Int? = -1, inDelete: MutableState<Boolean>) {
    var text by remember { mutableStateOf(curtext) }
    val mutableOffsetX = remember {mutableStateOf(offsetX)}
    val mutableOffsetY = remember {mutableStateOf(offsetY)}
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(WebSockets)
    }
    Box(
        modifier = Modifier
            .offset(mutableOffsetX.value.dp, mutableOffsetY.value.dp)
            .clickable(true) {
                println("Current inDelete state for textbox $Id is: $inDelete.value")
                if (inDelete.value) {
                    println("Deleting textbox $Id")
                    runBlocking {
                        try {
                            client.delete("http://localhost:8080/textbox/$Id")
                        }
                        catch (e: Exception) {
                            println(e)
                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        println("Websocket /receive-deleted-texbox-id")
                        val socket: DefaultClientWebSocketSession = client.webSocketSession(
                            method = HttpMethod.Get,
                            host = "127.0.0.1",
                            port = 8080,
                            path = "/receive-deleted-texbox-id"
                        )
                        println("TextBox to be deleted:$Id")
                        val dlTbJson = Json.encodeToString(Id)
                        socket.send(Frame.Text(dlTbJson))
                    }
                }
            }
            .pointerInput(Unit) {
                detectDragGestures (
                    onDrag = { change, dragAmount ->
                        change.consume()
                        mutableOffsetX.value += dragAmount.x.toInt() / 2
                        mutableOffsetY.value += dragAmount.y.toInt() / 2
                    },
                    onDragEnd = {
                        CoroutineScope(Dispatchers.IO).launch {
                            println("Websocket /receive-updated-textbox")
                            var newTBData = TextBoxData(mutableOffsetX.value, mutableOffsetY.value, curtext, color, size, Id)
                            val socket: DefaultClientWebSocketSession = client.webSocketSession(
                                method = HttpMethod.Get,
                                host = "127.0.0.1",
                                port = 8080,
                                path = "/receive-updated-textbox"
                            )
                            println("TextBox to be updated:$Id")
                            val TbJson = Json.encodeToString(newTBData)
                            socket.send(Frame.Text(TbJson))
                        }
                    }
                )

            }
            .padding(10.dp)
            .background(Color(color))
    ) {
        BasicTextField(
            modifier = Modifier
                .width(IntrinsicSize.Min),
            value = text,
            onValueChange = { text = it },
            textStyle = TextStyle(color = Color.Black, fontSize = (size*5).sp)
        )
    }

}