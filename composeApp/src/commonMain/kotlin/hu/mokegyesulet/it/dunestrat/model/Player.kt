package hu.mokegyesulet.it.dunestrat.model

data class Player(
    val id: String,
    val water: Int,
    val spice: Int,
    private val weapons: Map<Weapon, Int>,
    val ownedFields: List<Field>,
) {
    fun getWeaponCount(weapon: Weapon): Int = weapons[weapon] ?: 0
}
