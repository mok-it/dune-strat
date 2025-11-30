package hu.mokegyesulet.it.dunestrat.util

data class UnitVector(
    val x: Int,
    val y: Int,
) {

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
}

operator fun Int.times(absoluteVector: AbsoluteVector): AbsoluteVector = absoluteVector * this

fun main() {
    val coords = listOf("A1", "A2", "B1", "B2", "C2", "E6")
    var s = ""
    for (c in coords) {
        s += hexDrawer(
            UnitVector.fromDisplayCoordinate(c),

        ) + "\n"
    }
    println(s)
}
