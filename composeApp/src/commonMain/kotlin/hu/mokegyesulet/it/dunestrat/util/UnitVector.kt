package hu.mokegyesulet.it.dunestrat.util

data class UnitVector(
    val x: Int,
    val y: Int,
) {
    fun toAbsolute(baseX: AbsoluteVector, baseY: AbsoluteVector): AbsoluteVector =
        x * baseX + y * baseY
    operator fun times(number: Int): UnitVector = UnitVector(x * number, y * number)

    companion object { // ide beírni, hogy az A12-ből csináljon egy számpárt
        fun fromDisplayCoordinate(coord: String): UnitVector {
            val first = coord.lowercase()[0].code - 'a'.code
            val second = coord.substring(1).toInt() - 1 // így tesszük origóra
            return UnitVector(first, second)
        }
    }
}
data class AbsoluteVector(
    val x: Double,
    val y: Double,
) {
    operator fun plus(absvector: AbsoluteVector): AbsoluteVector = AbsoluteVector(
        x + absvector.x,
        y + absvector.y,
    )
    operator fun times(number: Int): AbsoluteVector = AbsoluteVector(x * number, y * number)
    companion object { // ide beírni, hogy az A12-ből csináljon egy számpárt
        fun fromDisplayCoordinate(coord: String): UnitVector {
            val first = coord.lowercase()[0].code - 'a'.code
            val second = coord.substring(1).toInt() - 1 // így tesszük origóra
            return UnitVector(first, second)
        }
    }
}

operator fun Int.times(absoluteVector: AbsoluteVector): AbsoluteVector = absoluteVector * this

val vecX = AbsoluteVector(144.0, -83.13843876)
val vecY = AbsoluteVector(0.0, 166.2768775)

fun main() {
    println(UnitVector.fromDisplayCoordinate("A3"))
    val vectorX = UnitVector.fromDisplayCoordinate("A3").x
    val vectorY = UnitVector.fromDisplayCoordinate("A3").y

    val newVecX = vecX * vectorX
    println("$newVecX")
    val newVecY = vecY * vectorY
    println("$newVecY")

    val coords = listOf("C2")
    var s = ""
    for (c in coords) {
        s += hexDrawer(
            UnitVector.fromDisplayCoordinate(c),
            newVecX,
            newVecY,
        ) + "\n"
        println("$s")
    }
}
