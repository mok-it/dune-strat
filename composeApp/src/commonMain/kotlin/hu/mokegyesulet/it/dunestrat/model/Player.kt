package hu.mokegyesulet.it.dunestrat.model

import kotlin.math.pow
import kotlinx.serialization.Transient

data class Player(
    val id: Int,
    var water: Int,
    var spice: Int,
    var harvestersPurchased: Int,
    val weapons: MutableMap<Weapon, Int>,
    val ownedFields: MutableSet<GameStateField>,
    @Transient
    var inDebt: Boolean = false,
) {
    /**
     * Returns the number of weapons the player owns for the given type.
     *
     * @param weapon the weapon type to look up
     * @return the number of owned weapons of that type, or `0` if none are owned
     */
    fun getWeaponCount(weapon: Weapon): Int = weapons[weapon] ?: 0

    /**
     * Removes ownership of the specified fields from the player.
     *
     * @param fields the ids of fields to leave
     */
    fun leaveFields(fields: Set<String>) {
        ownedFields.removeAll { field -> field.id in fields }
    }

    /**
     * Applies water consumption for all owned fields and resolves starvation if needed.
     *
     * @return `true` if the player had a water shortage, otherwise `false`
     */
    fun waterConsumption(): Boolean {
        var waterChange = 0
        ownedFields.forEach { field -> waterChange += field.water }
        water += waterChange

        // return whether there is an issue or not
        if (water >= 0) {
            return false
        }
        ownedFields.removeAll { field -> field.water < 0 }
        water = 0
        return true
    }

    /**
     * Deducts the total spice cost of the selected weapon purchases.
     *
     * @param purchaseWeapons the weapons the player wants to buy
     */
    fun purchaseWeapons(purchaseWeapons: Map<Weapon, Int>) {
        spice -= calculateWeaponPrices(purchaseWeapons)
    }

    /**
     * Adds purchased weapons to the player's inventory.
     *
     * @param purchaseWeapons the weapons to deliver to the player
     */
    fun deliverWeapons(purchaseWeapons: Map<Weapon, Int>) {
        purchaseWeapons.keys.forEach { weapon ->
            weapons[weapon] = getWeaponCount(weapon) + (purchaseWeapons[weapon] ?: 0)
        }
    }

    /**
     * Purchases and activates a harvester on the given field.
     *
     * @param purchaseField the field on which to build the harvester
     */
    fun purchaseHarvester(purchaseField: GameStateField) {
        purchaseField.harvester = true
        spice -= 5 * 2.0.pow(harvestersPurchased++).toInt()
    }

    /**
     * Calculates the spice cost for the given weapon purchases.
     *
     * @param purchaseWeapons the weapons to price out
     * @return the total spice cost
     */
    fun calculateWeaponPrices(purchaseWeapons: Map<Weapon, Int>): Int {
        var sum = 0
        purchaseWeapons.forEach { entry ->
            sum += entry.value * entry.key.price
        }
        return sum
    }

    /**
     * Calculates the spice cost for building the specified number of harvesters.
     *
     * @param count the number of harvesters to build
     * @return the total spice cost
     */
    fun calculateHarvesterPrice(count: Int): Int =
        (5 * 2.0.pow(harvestersPurchased) * (2.0.pow(count) - 1)).toInt()

    /**
     * Calculates the total cost of all purchases contained in the given player step.
     *
     * @param purchases the player step containing planned purchases
     * @return the total spice cost of the purchases
     */
    fun calculatePrices(purchases: PlayerStep): Int =
        calculateWeaponPrices(purchases.purchaseWeapons) +
            calculateHarvesterPrice(purchases.buildHarvesters.size)

    /**
     * Calculates the battle power of the player on a specific field.
     *
     * @param field the field for which power is being calculated
     * @return the total battle power on that field
     */
    fun calculatePower(field: GameStateField): Int {
        var sum = getWeaponCount(field.effectiveWeapon) * 2
        if (field !in ownedFields) {
            sum += getWeaponCount(Weapon.LEGION) * 3
        }
        weapons.filter { weapon -> weapon.key != Weapon.LEGION }.forEach { weapon ->
            sum += weapon.value
        }
        return sum
    }

    /**
     * Determines whether the player can reach the given field from any owned field.
     *
     * @param fieldInQuestion the field to test for reachability
     * @return `true` if the field is reachable, otherwise `false`
     */
    fun isFieldReachable(fieldInQuestion: GameStateField): Boolean {
        if (fieldInQuestion in ownedFields) {
            return false
        }
        ownedFields.forEach { field ->
            if (fieldInQuestion in field.neighbours) {
                return true
            }
        }
        return false
    }

    /**
     * Reduces each weapon count by the given percentage.
     *
     * @param percent the percentage of weapons to lose, where values above `1.0` are clamped to `1.0`
     */
    fun loseWeaponPrecent(percent: Double) {
        val normalizedPercent = if (percent > 1.0) 1.0 else percent
        weapons.keys.forEach { weapon ->
            val ownedWeapons = getWeaponCount(weapon)
            val lostWeapons = (ownedWeapons * normalizedPercent).toInt()
            weapons[weapon] = ownedWeapons - lostWeapons
        }
    }
}
