package hu.mokegyesulet.it.dunestrat.util.drawmap

import kotlin.math.cos
import kotlin.math.sin

data class Vector2D(
    val x: Double,
    val y: Double,
) {
    operator fun times(scalar: Double): Vector2D = Vector2D(x * scalar, y * scalar)
    operator fun times(scalar: Int): Vector2D = this * scalar.toDouble()

    operator fun plus(other: Vector2D): Vector2D = Vector2D(x + other.x, y + other.y)

    operator fun minus(other: Vector2D): Vector2D = Vector2D(x - other.x, y - other.y)

    operator fun div(scalar: Double): Vector2D = this * (1 / scalar)
    operator fun div(scalar: Int): Vector2D = this / scalar.toDouble()

    fun rotate(angle: Double): Vector2D {
        val cosAngle = cos(angle)
        val sinAngle = sin(angle)
        return Vector2D(
            x * cosAngle - y * sinAngle,
            x * sinAngle + y * cosAngle,
        )
    }

    fun invertY(): Vector2D = Vector2D(x, -y)
}

operator fun Double.times(vector: Vector2D): Vector2D = vector * this
operator fun Int.times(vector: Vector2D): Vector2D = vector * this
