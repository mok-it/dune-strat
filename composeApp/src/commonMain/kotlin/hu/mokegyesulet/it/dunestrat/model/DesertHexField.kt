package hu.mokegyesulet.it.dunestrat.model

data class DesertHexField(
    val id: String,
    val q: Int, // column
    val r: Int, // diagonal row
    val water: Int,
    val spice: Int,
    val effectiveWeapon: Weapon,
    val startingField: Boolean,
) {
    // cube coord third axis
    val s: Int get() = -q - r

    // the 6 mathematically correct neighbour coords
    fun neighbourCoords(): List<Pair<Int, Int>> {
        val dirs = listOf(
            1 to 0,
            1 to -1,
            0 to -1,
            -1 to 0,
            -1 to 1,
            0 to 1,
        )
        return dirs.map { (dq, dr) -> (q + dq) to (r + dr) }
    }
}
