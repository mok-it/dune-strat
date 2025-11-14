package hu.mokegyesulet.it.dunestrat.model

import io.github.aakira.napier.Napier
import kotlin.collections.set
import kotlin.math.abs

class Desert {
    private val fields = mutableMapOf<Pair<Int, Int>, DesertHexField>()

    fun add(field: DesertHexField) {
        fields[field.q to field.r] = field
    }

    fun get(q: Int, r: Int): DesertHexField? = fields[q to r]

    fun neighboursOf(field: DesertHexField): List<DesertHexField> =
        field.neighbourCoords().mapNotNull { (q, r) -> fields[q to r] }

    companion object {
        /**
         Function to generate a 12 player hexagon map.
         */
        fun create12PlayerHexagon(radius: Int = 8): Desert {
            val desert = Desert()

            for (q in -radius..radius) {
                for (r in -radius..radius) {
                    val s = -q - r
                    if (abs(s) <= radius) {
                        val field = DesertHexField(
                            id = "${q}_$r",
                            q = q,
                            r = r,
                            water = getRandomWaterValue(),
                            spice = getRandomSpiceValue(),
                            effectiveWeapon = getRandomWeapon(),
                            startingField = false,
                        )
                        desert.add(field)
                    }
                }
            }

            Napier.d("Number of fields: ${desert.fields.size}")

            return desert
        }

        // TODO fine-tune these values
        private fun getRandomWaterValue(): Int = (-7..-1).random()

        private fun getRandomSpiceValue(): Int = (1..6).random()

        private fun getRandomWeapon(): Weapon = Weapon.entries.toTypedArray().random()
    }
}
