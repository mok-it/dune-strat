package hu.mokegyesulet.it.dunestrat.model

data class Player(
    val id: String,
    val water: Int,
    val spice: Int,
    private val weapons: Map<Weapon, Int>,
    val ownedFields: Set<GameStateField>,
) {
    fun getWeaponCount(weapon: Weapon): Int = weapons[weapon] ?: 0

    fun leaveFields(fields: Set<GameStateField>): Player {
        return this.copy(ownedFields = ownedFields - fields)
    }

    fun waterConsumption(): Pair<Player, Boolean> {
        var waterChange = 0
        ownedFields.forEach { field -> waterChange += field.water }
        val newWater = this.water + waterChange
        if (newWater >= 0) {
            return this.copy(water = newWater) to false
        }

        return this.leaveFields(ownedFields.filter { field -> field.water < 0 }.toSet()).copy(water = 0) to true
    }

    fun purchaseWeapons(purchaseWeapons: Map<Weapon, Int>, purchasePrice: Int): Player {
        val newWeapons = weapons.toMutableMap()
        purchaseWeapons.forEach { weapon ->
            newWeapons[weapon.key] = getWeaponCount(weapon.key) + weapon.value
        }

        return this.copy(weapons = newWeapons, spice = spice - purchasePrice)
    }

}
