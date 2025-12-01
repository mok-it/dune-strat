package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.math.abs

@Serializable
data class Desert(
    @Transient
    val id: Int? = null,
    val fields: Set<DesertField>,
) {

    companion object {
        fun create12PlayerHexagon(): Desert {
            val fields = mutableSetOf<DesertField>()

            val desertValuesByDistance = mapOf(
                0 to Pair(-8, 40),
                1 to Pair(-7, 37),
                2 to Pair(-6, 29),
                3 to Pair(-5, 19),
                4 to Pair(-4, 11),
                5 to Pair(-3, 5),
                6 to Pair(-2, 2),
                7 to Pair(-1, 1),
            )

            for (x in -7..7) {
                for (y in -14..14) {
                    val distance = abs(x) + abs(y)
                    if (distance % 2 == 0 && distance / 2 <= 7) {
                        val values = desertValuesByDistance[distance / 2] ?: Pair(0, 0)

                        val desertField = DesertField(
                            id = "${'B' + x + 7}${(y + 14 + 2) / 2}",
                            water = values.first,
                            spice = values.second,
                            effectiveWeapon = Weapon.LEGION, // TODO
                            neighbours = setOf(),
                            startingField = false, // TODO
                        )

                        println("($x, $y): $desertField")

                        fields.add(desertField)
                    }
                }
            }

            return Desert(fields)
        }
    }
}
