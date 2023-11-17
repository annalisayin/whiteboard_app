package models

import kotlinx.serialization.Serializable

@Serializable
data class Sketch(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val color: Int,
    val width: Int,
)