package models

import kotlinx.serialization.Serializable

@Serializable
class Circle(override var x: Float, override var y: Float, override var color: Int, override var size: Int) : Shape()
