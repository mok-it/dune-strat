package hu.mokegyesulet.it.dunestrat.model

import kotlin.math.abs
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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

            // exelből kimásolt
            // ürres mezőnél Legion a hatásos
            val effectiveWeaponsByIdOldCoordinates = mapOf(
                "A3" to Weapon.PISTOL,
                "A7" to Weapon.CRYSKNIFE,
                "B2" to Weapon.CRYSKNIFE,
                "B3" to Weapon.PISTOL,
                "B4" to Weapon.PISTOL,
                "B5" to Weapon.PISTOL,
                "B6" to Weapon.CRYSKNIFE,
                "B7" to Weapon.CRYSKNIFE,
                "B8" to Weapon.CRYSKNIFE,
                "B9" to Weapon.PISTOL,
                "C1" to Weapon.LASGUN,
                "C2" to Weapon.LASGUN,
                "C3" to Weapon.CRYSKNIFE,
                "C4" to Weapon.PISTOL,
                "C5" to Weapon.PISTOL,
                "C6" to Weapon.LASGUN,
                "C7" to Weapon.CRYSKNIFE,
                "C8" to Weapon.CRYSKNIFE,
                "C9" to Weapon.PISTOL,
                "C10" to Weapon.LASGUN,
                "C11" to Weapon.LASGUN,
                "D2" to Weapon.LASGUN,
                "D3" to Weapon.LASGUN,
                "D4" to Weapon.CRYSKNIFE,
                "D5" to Weapon.PISTOL,
                "D6" to Weapon.PISTOL,
                "D7" to Weapon.CRYSKNIFE,
                "D8" to Weapon.CRYSKNIFE,
                "D9" to Weapon.PISTOL,
                "D10" to Weapon.LASGUN,
                "D11" to Weapon.LASGUN,
                "E2" to Weapon.LASGUN,
                "E3" to Weapon.LASGUN,
                "E4" to Weapon.LASGUN,
                "E5" to Weapon.CRYSKNIFE,
                "E6" to Weapon.PISTOL,
                "E7" to Weapon.LASGUN,
                "E8" to Weapon.CRYSKNIFE,
                "E9" to Weapon.PISTOL,
                "E10" to Weapon.LASGUN,
                "E11" to Weapon.LASGUN,
                "E12" to Weapon.LASGUN,
                "F2" to Weapon.CRYSKNIFE,
                "F3" to Weapon.PISTOL,
                "F4" to Weapon.LASGUN,
                "F5" to Weapon.LASGUN,
                "F6" to Weapon.CRYSKNIFE,
                "F7" to Weapon.PISTOL,
                "F8" to Weapon.CRYSKNIFE,
                "F9" to Weapon.PISTOL,
                "F10" to Weapon.LASGUN,
                "F11" to Weapon.LASGUN,
                "F12" to Weapon.CRYSKNIFE,
                "F13" to Weapon.PISTOL,
                "G1" to Weapon.CRYSKNIFE,
                "G2" to Weapon.CRYSKNIFE,
                "G3" to Weapon.CRYSKNIFE,
                "G4" to Weapon.CRYSKNIFE,
                "G5" to Weapon.PISTOL,
                "G6" to Weapon.LASGUN,
                "G7" to Weapon.LASGUN,
                "G8" to Weapon.PISTOL,
                "G9" to Weapon.CRYSKNIFE,
                "G10" to Weapon.LASGUN,
                "G11" to Weapon.CRYSKNIFE,
                "G12" to Weapon.PISTOL,
                "G13" to Weapon.PISTOL,
                "G14" to Weapon.PISTOL,
                "G15" to Weapon.PISTOL,
                "H2" to Weapon.CRYSKNIFE,
                "H3" to Weapon.CRYSKNIFE,
                "H4" to Weapon.CRYSKNIFE,
                "H5" to Weapon.CRYSKNIFE,
                "H6" to Weapon.CRYSKNIFE,
                "H7" to Weapon.CRYSKNIFE,
                "H8" to Weapon.CRYSKNIFE,
                "H9" to Weapon.PISTOL,
                "H10" to Weapon.LASGUN,
                "H11" to Weapon.PISTOL,
                "H12" to Weapon.PISTOL,
                "H13" to Weapon.PISTOL,
                "H14" to Weapon.PISTOL,
                "H15" to Weapon.PISTOL,
                "I2" to Weapon.LASGUN,
                "I3" to Weapon.LASGUN,
                "I4" to Weapon.LASGUN,
                "I5" to Weapon.LASGUN,
                "I6" to Weapon.LASGUN,
                "I7" to Weapon.PISTOL,
                "I8" to Weapon.PISTOL,
                "I9" to Weapon.LASGUN,
                "I10" to Weapon.CRYSKNIFE,
                "I11" to Weapon.PISTOL,
                "I12" to Weapon.LASGUN,
                "I13" to Weapon.LASGUN,
                "I14" to Weapon.LASGUN,
                "I15" to Weapon.LASGUN,
                "I16" to Weapon.LASGUN,
                "J2" to Weapon.PISTOL,
                "J3" to Weapon.PISTOL,
                "J4" to Weapon.PISTOL,
                "J5" to Weapon.PISTOL,
                "J6" to Weapon.PISTOL,
                "J7" to Weapon.CRYSKNIFE,
                "J8" to Weapon.CRYSKNIFE,
                "J9" to Weapon.PISTOL,
                "J10" to Weapon.LASGUN,
                "J11" to Weapon.CRYSKNIFE,
                "J12" to Weapon.CRYSKNIFE,
                "J13" to Weapon.CRYSKNIFE,
                "J14" to Weapon.CRYSKNIFE,
                "J15" to Weapon.CRYSKNIFE,
                "K1" to Weapon.PISTOL,
                "K2" to Weapon.PISTOL,
                "K3" to Weapon.PISTOL,
                "K4" to Weapon.PISTOL,
                "K5" to Weapon.CRYSKNIFE,
                "K6" to Weapon.LASGUN,
                "K7" to Weapon.LASGUN,
                "K8" to Weapon.CRYSKNIFE,
                "K9" to Weapon.PISTOL,
                "K10" to Weapon.LASGUN,
                "K11" to Weapon.PISTOL,
                "K12" to Weapon.CRYSKNIFE,
                "K13" to Weapon.CRYSKNIFE,
                "K14" to Weapon.CRYSKNIFE,
                "K15" to Weapon.CRYSKNIFE,
                "L2" to Weapon.PISTOL,
                "L3" to Weapon.CRYSKNIFE,
                "L4" to Weapon.LASGUN,
                "L5" to Weapon.LASGUN,
                "L6" to Weapon.PISTOL,
                "L7" to Weapon.CRYSKNIFE,
                "L8" to Weapon.PISTOL,
                "L9" to Weapon.CRYSKNIFE,
                "L10" to Weapon.LASGUN,
                "L11" to Weapon.LASGUN,
                "L12" to Weapon.PISTOL,
                "L13" to Weapon.CRYSKNIFE,
                "M2" to Weapon.LASGUN,
                "M3" to Weapon.LASGUN,
                "M4" to Weapon.LASGUN,
                "M5" to Weapon.PISTOL,
                "M6" to Weapon.CRYSKNIFE,
                "M7" to Weapon.LASGUN,
                "M8" to Weapon.PISTOL,
                "M9" to Weapon.CRYSKNIFE,
                "M10" to Weapon.LASGUN,
                "M11" to Weapon.LASGUN,
                "M12" to Weapon.LASGUN,
                "N2" to Weapon.LASGUN,
                "N3" to Weapon.LASGUN,
                "N4" to Weapon.PISTOL,
                "N5" to Weapon.CRYSKNIFE,
                "N6" to Weapon.CRYSKNIFE,
                "N7" to Weapon.PISTOL,
                "N8" to Weapon.PISTOL,
                "N9" to Weapon.CRYSKNIFE,
                "N10" to Weapon.LASGUN,
                "N11" to Weapon.LASGUN,
                "O1" to Weapon.LASGUN,
                "O2" to Weapon.LASGUN,
                "O3" to Weapon.PISTOL,
                "O4" to Weapon.CRYSKNIFE,
                "O5" to Weapon.CRYSKNIFE,
                "O6" to Weapon.LASGUN,
                "O7" to Weapon.PISTOL,
                "O8" to Weapon.PISTOL,
                "O9" to Weapon.CRYSKNIFE,
                "O10" to Weapon.LASGUN,
                "O11" to Weapon.LASGUN,
                "P2" to Weapon.PISTOL,
                "P3" to Weapon.CRYSKNIFE,
                "P4" to Weapon.CRYSKNIFE,
                "P5" to Weapon.CRYSKNIFE,
                "P6" to Weapon.PISTOL,
                "P7" to Weapon.PISTOL,
                "P8" to Weapon.PISTOL,
                "P9" to Weapon.CRYSKNIFE,
                "Q3" to Weapon.CRYSKNIFE,
                "Q7" to Weapon.PISTOL,
            )

            val effectiveWeaponsById = effectiveWeaponsByIdOldCoordinates.mapKeys {
                convertCoordinateToNewSystem(it.key)
            }

            // exelből
            val basesOldCoordinates = setOf(
                "A3",
                "A7",
                "C1",
                "C10",
                "G1",
                "G15",
                "K1",
                "K15",
                "O1",
                "O11",
                "Q3",
                "Q7",
            )

            // exelből
            val mountainsOldCoordinates = mapOf(
                "A3"	to 3,
                "A7"	to 3,
                "B2"	to 7,
                "B9"	to 7,
                "C1"	to 3,
                "C5"	to 6,
                "C6"	to 6,
                "C7"	to 6,
                "C11"	to 3,
                "D6"	to 5,
                "D7"	to 5,
                "E3"	to 6,
                "E11"	to 6,
                "F3"	to 6,
                "F4"	to 5,
                "F6"	to 3,
                "F9"	to 3,
                "F11"	to 5,
                "F12"	to 6,
                "G1"	to 3,
                "G3"	to 6,
                "G4"	to 5,
                "G12"	to 5,
                "G13"	to 6,
                "G15"	to 3,
                "I2"	to 7,
                "I6"	to 3,
                "I12"	to 3,
                "I16"	to 7,
                "K1"	to 3,
                "K3"	to 6,
                "K4"	to 5,
                "K12"	to 5,
                "K13"	to 6,
                "K15"	to 3,
                "L3"	to 6,
                "L4"	to 5,
                "L6"	to 3,
                "L9"	to 3,
                "L11"	to 5,
                "L12"	to 6,
                "M3"	to 6,
                "M11"	to 6,
                "N6"	to 5,
                "N7"	to 5,
                "O1"	to 3,
                "O5"	to 6,
                "O6"	to 6,
                "O7"	to 6,
                "O11"	to 3,
                "P2"	to 7,
                "P9"	to 7,
                "Q3"	to 3,
                "Q7"	to 3,
            )

            val bases = basesOldCoordinates.map {
                convertCoordinateToNewSystem(it)
            }.toSet()

            val mountains = mountainsOldCoordinates.mapKeys {
                convertCoordinateToNewSystem(it.key)
            }

            val size = 7

            for (x in -size..size) {
                for (y in -size * 2..size * 2) {
                    val distance = abs(x) + abs(y)
                    if (distance % 2 == 0 && distance / 2 <= size) {
                        val stepsToReach =
                            abs(x) + if (abs(y) > abs(x)) (abs(y) - abs(x)) / 2 else 0

                        val values = desertValuesByDistance[stepsToReach] ?: Pair(0, 0)

                        val id = "${'B' + x + size}${(y + (size * 2) + 2) / 2}"

                        val desertField = DesertField(
                            id = id,
                            water = if (id in mountains.keys) {
                                mountains[id]!!
                            } else {
                                values.first
                            },
                            spice = if (id in mountains.keys) {
                                mountains[id]!!
                            } else {
                                values.second
                            },
                            effectiveWeapon = effectiveWeaponsById[id] ?: Weapon.LEGION,
                            neighbours = mutableSetOf(),
                            startingField = id in bases,
                        )

                        fields.add(desertField)
                    }
                }
            }

            bases.forEach { id ->
                val desertField = DesertField(
                    id = id,
                    water = 4,
                    spice = 0,
                    effectiveWeapon = effectiveWeaponsById[id]!!,
                    neighbours = mutableSetOf(),
                    startingField = true,
                )
                fields.add(desertField)
            }

            val newFields = mutableSetOf<DesertField>()

            fields.forEach { field ->
                val neighbours = mutableSetOf<DesertField>()

                val fieldFirstCord = field.id[0].code
                val fieldSecondCord = field.id.substring(1).toInt()

                val neighbourFirstCords = fieldFirstCord - 1..fieldFirstCord + 1

                val neighbourSecondCords = fieldSecondCord - 1..fieldSecondCord + 1

                fields.filter { entry ->
                    (entry.id[0].code in neighbourFirstCords) and
                        (entry.id.substring(1).toInt() in neighbourSecondCords) and
                        !(
                            (
                                (entry.id[0].code == fieldFirstCord - 1) and
                                    (entry.id.substring(1).toInt() == fieldSecondCord + 1)
                                ) or (
                                (entry.id[0].code == fieldFirstCord + 1) and
                                    (entry.id.substring(1).toInt() == fieldSecondCord - 1)
                                ) or (
                                (entry.id[0].code == fieldFirstCord) and
                                    (entry.id.substring(1).toInt() == fieldSecondCord)
                                )
                            )
                }.forEach { entry ->
                    neighbours.add(entry)
                }

                newFields.add(
                    DesertField(
                        field.id,
                        field.water,
                        field.spice,
                        field.effectiveWeapon,
                        neighbours,
                        field.startingField,
                    ),
                )
            }

            return Desert(fields = newFields)
        }

        fun convertCoordinateToNewSystem(
            oldCoordinate: String,
            middleColumnLetter: Char = 'I',
        ): String {
            val letter = oldCoordinate[0]
            val number = oldCoordinate.substring(1).toInt()
            if (letter <= middleColumnLetter) {
                return oldCoordinate
            }
            val shift = letter - middleColumnLetter
            val newNumber = number + shift
            return "$letter$newNumber"
        }
    }
}
