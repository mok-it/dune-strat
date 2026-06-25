package hu.mokegyesulet.it.dunestrat.model

data class Desert(
    val id: Int = -1,
    val name: String = "",
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
                "C11",
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

            val oldValues = mapOf(
                "A3" to Pair(3, 0),
                "A7" to Pair(3, 0),
                "B2" to Pair(7, 0),
                "B3" to Pair(-1, 1),
                "B4" to Pair(-1, 1),
                "B5" to Pair(-1, 1),
                "B6" to Pair(-1, 1),
                "B7" to Pair(-1, 1),
                "B8" to Pair(-1, 1),
                "B9" to Pair(7, 0),
                "C1" to Pair(3, 0),
                "C2" to Pair(-1, 1),
                "C3" to Pair(-2, 2),
                "C4" to Pair(-2, 2),
                "C5" to Pair(6, 0),
                "C6" to Pair(6, 0),
                "C7" to Pair(6, 0),
                "C8" to Pair(-2, 2),
                "C9" to Pair(-2, 2),
                "C10" to Pair(-1, 1),
                "C11" to Pair(3, 0),
                "D2" to Pair(-1, 1),
                "D3" to Pair(-2, 2),
                "D4" to Pair(-3, 5),
                "D5" to Pair(-3, 5),
                "D6" to Pair(5, 0),
                "D7" to Pair(5, 0),
                "D8" to Pair(-3, 5),
                "D9" to Pair(-3, 5),
                "D10" to Pair(-2, 2),
                "D11" to Pair(-1, 1),
                "E2" to Pair(-1, 1),
                "E3" to Pair(6, 0),
                "E4" to Pair(-3, 5),
                "E5" to Pair(-4, 11),
                "E6" to Pair(-4, 11),
                "E7" to Pair(-4, 11),
                "E8" to Pair(-4, 11),
                "E9" to Pair(-4, 11),
                "E10" to Pair(-3, 5),
                "E11" to Pair(6, 0),
                "E12" to Pair(-1, 1),
                "F2" to Pair(-1, 1),
                "F3" to Pair(6, 0),
                "F4" to Pair(5, 0),
                "F5" to Pair(-4, 11),
                "F6" to Pair(3, 0),
                "F7" to Pair(-5, 19),
                "F8" to Pair(-5, 19),
                "F9" to Pair(3, 0),
                "F10" to Pair(-4, 11),
                "F11" to Pair(5, 0),
                "F12" to Pair(6, 0),
                "F13" to Pair(-1, 1),
                "G1" to Pair(3, 0),
                "G2" to Pair(-1, 1),
                "G3" to Pair(6, 0),
                "G4" to Pair(5, 0),
                "G5" to Pair(-4, 11),
                "G6" to Pair(-5, 19),
                "G7" to Pair(-6, 29),
                "G8" to Pair(-6, 29),
                "G9" to Pair(-6, 29),
                "G10" to Pair(-5, 19),
                "G11" to Pair(-4, 11),
                "G12" to Pair(5, 0),
                "G13" to Pair(6, 0),
                "G14" to Pair(-1, 1),
                "G15" to Pair(3, 0),
                "H2" to Pair(-1, 1),
                "H3" to Pair(-2, 2),
                "H4" to Pair(-3, 5),
                "H5" to Pair(-4, 11),
                "H6" to Pair(-5, 19),
                "H7" to Pair(-6, 29),
                "H8" to Pair(-7, 37),
                "H9" to Pair(-7, 37),
                "H10" to Pair(-6, 29),
                "H11" to Pair(-5, 19),
                "H12" to Pair(-4, 11),
                "H13" to Pair(-3, 5),
                "H14" to Pair(-2, 2),
                "H15" to Pair(-1, 1),
                "I2" to Pair(7, 0),
                "I3" to Pair(-2, 2),
                "I4" to Pair(-3, 5),
                "I5" to Pair(-4, 11),
                "I6" to Pair(3, 0),
                "I7" to Pair(-6, 29),
                "I8" to Pair(-7, 37),
                "I9" to Pair(-8, 40),
                "I10" to Pair(-7, 37),
                "I11" to Pair(-6, 29),
                "I12" to Pair(3, 0),
                "I13" to Pair(-4, 11),
                "I14" to Pair(-3, 5),
                "I15" to Pair(-2, 2),
                "I16" to Pair(7, 0),
                "J2" to Pair(-1, 1),
                "J3" to Pair(-2, 2),
                "J4" to Pair(-3, 5),
                "J5" to Pair(-4, 11),
                "J6" to Pair(-5, 19),
                "J7" to Pair(-6, 29),
                "J8" to Pair(-7, 37),
                "J9" to Pair(-7, 37),
                "J10" to Pair(-6, 29),
                "J11" to Pair(-5, 19),
                "J12" to Pair(-4, 11),
                "J13" to Pair(-3, 5),
                "J14" to Pair(-2, 2),
                "J15" to Pair(-1, 1),
                "K1" to Pair(3, 0),
                "K2" to Pair(-1, 1),
                "K3" to Pair(6, 0),
                "K4" to Pair(5, 0),
                "K5" to Pair(-4, 11),
                "K6" to Pair(-5, 19),
                "K7" to Pair(-6, 29),
                "K8" to Pair(-6, 29),
                "K9" to Pair(-6, 29),
                "K10" to Pair(-5, 19),
                "K11" to Pair(-4, 11),
                "K12" to Pair(5, 0),
                "K13" to Pair(6, 0),
                "K14" to Pair(-1, 1),
                "K15" to Pair(3, 0),
                "L2" to Pair(-1, 1),
                "L3" to Pair(6, 0),
                "L4" to Pair(5, 0),
                "L5" to Pair(-4, 11),
                "L6" to Pair(3, 0),
                "L7" to Pair(-5, 19),
                "L8" to Pair(-5, 19),
                "L9" to Pair(3, 0),
                "L10" to Pair(-4, 11),
                "L11" to Pair(5, 0),
                "L12" to Pair(6, 0),
                "L13" to Pair(-1, 1),
                "M2" to Pair(-1, 1),
                "M3" to Pair(6, 0),
                "M4" to Pair(-3, 5),
                "M5" to Pair(-4, 11),
                "M6" to Pair(-4, 11),
                "M7" to Pair(-4, 11),
                "M8" to Pair(-4, 11),
                "M9" to Pair(-4, 11),
                "M10" to Pair(-3, 5),
                "M11" to Pair(6, 0),
                "M12" to Pair(-1, 1),
                "N2" to Pair(-1, 1),
                "N3" to Pair(-2, 2),
                "N4" to Pair(-3, 5),
                "N5" to Pair(-3, 5),
                "N6" to Pair(5, 0),
                "N7" to Pair(5, 0),
                "N8" to Pair(-3, 5),
                "N9" to Pair(-3, 5),
                "N10" to Pair(-2, 2),
                "N11" to Pair(-1, 1),
                "O1" to Pair(3, 0),
                "O2" to Pair(-1, 1),
                "O3" to Pair(-2, 2),
                "O4" to Pair(-2, 2),
                "O5" to Pair(6, 0),
                "O6" to Pair(6, 0),
                "O7" to Pair(6, 0),
                "O8" to Pair(-2, 2),
                "O9" to Pair(-2, 2),
                "O10" to Pair(-1, 1),
                "O11" to Pair(3, 0),
                "P2" to Pair(7, 0),
                "P3" to Pair(-1, 1),
                "P4" to Pair(-1, 1),
                "P5" to Pair(-1, 1),
                "P6" to Pair(-1, 1),
                "P7" to Pair(-1, 1),
                "P8" to Pair(-1, 1),
                "P9" to Pair(7, 0),
                "Q3" to Pair(3, 0),
                "Q7" to Pair(3, 0),
            )

            val values = oldValues.mapKeys {
                convertCoordinateToNewSystem(it.key)
            }

            effectiveWeaponsById.forEach { (id, weapon) ->
                if (id in bases) {
                    fields.add(
                        DesertField(
                            id = id,
                            water = 4,
                            spice = 0,
                            effectiveWeapon = weapon,
                            neighbours = mutableSetOf(),
                            startingField = true,
                        ),
                    )
                } else if (id in mountains.keys) {
                    fields.add(
                        DesertField(
                            id = id,
                            water = mountains[id]!!,
                            spice = 0,
                            effectiveWeapon = weapon,
                            neighbours = mutableSetOf(),
                            startingField = false,
                        ),
                    )
                } else {
                    val (water, spice) = values[id] ?: Pair(-1, 1)

                    fields.add(
                        DesertField(
                            id = id,
                            water = water,
                            spice = spice,
                            effectiveWeapon = weapon,
                            neighbours = mutableSetOf(),
                            startingField = false,
                        ),
                    )
                }
            }

            effectiveWeaponsById.forEach { (id, weapon) ->
                if (id in bases) {
                    fields.add(
                        DesertField(
                            id = id,
                            water = 4,
                            spice = 0,
                            effectiveWeapon = weapon,
                            neighbours = mutableSetOf(),
                            startingField = true,
                        ),
                    )
                } else if (id in mountains.keys) {
                    fields.add(
                        DesertField(
                            id = id,
                            water = mountains[id]!!,
                            spice = 0,
                            effectiveWeapon = weapon,
                            neighbours = mutableSetOf(),
                            startingField = false,
                        ),
                    )
                } else {
                    val (water, spice) = values[id] ?: Pair(-1, 1)

                    fields.add(
                        DesertField(
                            id = id,
                            water = water,
                            spice = spice,
                            effectiveWeapon = weapon,
                            neighbours = mutableSetOf(),
                            startingField = false,
                        ),
                    )
                }
            }

            fields.forEach { field ->

                val fieldFirstCord = field.id[0].code
                val fieldSecondCord = field.id.substring(1).toInt()

                val neighbourFirstCords = fieldFirstCord - 1..fieldFirstCord + 1

                val neighbourSecondCords = fieldSecondCord - 1..fieldSecondCord + 1

                val neighbours = fields.filter { entry ->
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
                }

                field.neighbours.addAll(neighbours)
            }

            return Desert(
                name = "Standard 12 játékos",
                fields = fields,
            )
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

        fun create6PlayerTestHexagon(): Desert {
            val outer: (
                String,
            ) -> DesertField = { id ->
                DesertField(
                    id,
                    5,
                    0,
                    Weapon.LASGUN,
                    mutableSetOf(),
                    true,
                )
            }
            val inner: (
                String,
            ) -> DesertField = { id ->
                DesertField(
                    id,
                    -2,
                    5,
                    Weapon.PISTOL,
                    mutableSetOf(),
                    false,
                )
            }
            val fields = mutableSetOf<DesertField>()
            val center = DesertField(
                "C3",
                -5,
                15,
                Weapon.CRYSKNIFE,
                mutableSetOf(),
                false,
            )
            fields.add(center)
            listOf("B2", "C2", "D3", "D4", "C4", "B3").forEach { id ->
                val f = inner(id)
                val prev = fields.last()
                prev.neighbours.add(f)
                f.neighbours.add(prev)
                fields.add(f)
                f.neighbours.add(center)
                center.neighbours.add(f)
                if (id == "B3") {
                    val b2 = fields.first { it.id == "B2" }
                    f.neighbours.add(b2)
                    b2.neighbours.add(f)
                }
            }
            val b2 = fields.first { it.id == "B2" }
            val c2 = fields.first { it.id == "C2" }
            val d3 = fields.first { it.id == "D3" }
            val d4 = fields.first { it.id == "D4" }
            val c4 = fields.first { it.id == "C4" }
            val b3 = fields.first { it.id == "B3" }
            val b1 = outer("B1")
            val d2 = outer("D2")
            val e4 = outer("E4")
            val d5 = outer("D5")
            val b4 = outer("B4")
            val a2 = outer("A2")
            fields.addAll(listOf(b1, d2, e4, d5, b4, a2))
            b1.neighbours.add(b2)
            b2.neighbours.add(b1)
            b1.neighbours.add(c2)
            c2.neighbours.add(b1)
            d2.neighbours.add(c2)
            c2.neighbours.add(d2)
            d2.neighbours.add(d3)
            d3.neighbours.add(d2)
            e4.neighbours.add(d3)
            d3.neighbours.add(e4)
            e4.neighbours.add(d4)
            d4.neighbours.add(e4)
            d5.neighbours.add(d4)
            d4.neighbours.add(d5)
            d5.neighbours.add(c4)
            c4.neighbours.add(d5)
            b4.neighbours.add(c4)
            c4.neighbours.add(b4)
            b4.neighbours.add(b3)
            b3.neighbours.add(b4)
            a2.neighbours.add(b3)
            b3.neighbours.add(a2)
            a2.neighbours.add(b2)
            b2.neighbours.add(a2)
            return Desert(fields = fields, name = "Teszt (6 játékos)")
        }
    }
}
