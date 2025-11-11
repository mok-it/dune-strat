package hu.mokegyesulet.it.dunestrat.model

class Player(
    val id: String,
    var water: Int,
    var spice: Int,
    private val weapons: MutableMap<Weapon, Int>,
    val ownedFields: MutableSet<GameStateField>,
) {
    fun getWeaponCount(weapon: Weapon): Int = weapons[weapon] ?: 0

    fun leaveFields(fields: Set<GameStateField>) {
        ownedFields.removeAll(fields)
    }

    fun waterConsumption(): Boolean {
        var waterChange = 0
        ownedFields.forEach { field -> waterChange += field.water }
        water += waterChange
        if (water >= 0) {
            return false
        }
        leaveFields(ownedFields.filter { field -> field.water < 0 }.toSet())
        return true
    }

    fun purchaseWeapons(purchaseWeapons: Map<Weapon, Int>, purchasePrice: Int) {
        purchaseWeapons.forEach { weapon ->
            weapons[weapon.key] = getWeaponCount(weapon.key) + weapon.value
        }

        spice -= purchasePrice
    }
}
