package models

import kotlinx.serialization.Serializable

@Serializable
abstract class Shape {
    abstract var x: Float
    abstract var y: Float
    abstract var color: Int
    abstract var size: Int
}