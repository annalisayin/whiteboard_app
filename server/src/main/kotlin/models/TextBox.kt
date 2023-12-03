package models

import kotlinx.serialization.Serializable

@Serializable
data class TextBox(
    val offsetX: Int,
    val offsetY: Int,
    val curtext: String,
    val color: Int,
    val size: Int,
    var Id: Int? = -1,
)
