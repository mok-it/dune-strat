package hu.mokegyesulet.it.dunestrat.model

import kotlin.collections.forEach
import kotlin.math.pow

class Player(
    val id: String,
    var water: Int,
    var spice: Int,
    var harvestersPurchased: Int,
    private val weapons: MutableMap<Weapon, Int>,
    val ownedFields: MutableSet<GameStateField>,
    var inDebt: Boolean,
) {
    fun getWeaponCount(weapon: Weapon): Int = weapons[weapon] ?: 0

    fun leaveFields(fields: Set<GameStateField>) {
        ownedFields.removeAll(fields)
    }

    fun waterConsumption(): Boolean {
        var waterChange = 0
        ownedFields.forEach { field -> waterChange += field.water }
        water += waterChange

        // return whether there is an issue or not
        if (water >= 0) {
            return false
        }
        leaveFields(ownedFields.filter { field -> field.water < 0 }.toSet())
        water = 0
        return true
    }

    fun purchaseWeapons(purchaseWeapons: Map<Weapon, Int>) {
        spice -= calculateWeaponPrices(purchaseWeapons)
    }

    fun deliverWeapons(purchaseWeapons: Map<Weapon, Int>) {
        purchaseWeapons.keys.forEach { weapon ->
            weapons[weapon] = getWeaponCount(weapon) + (purchaseWeapons[weapon] ?: 0)
        }
    }

    fun purchaseHarvester(purchaseField: GameStateField) {
        purchaseField.harvester = true
        spice -= 5 * 2.toDouble().pow(harvestersPurchased++).toInt()
    }

    fun calculateWeaponPrices(purchaseWeapons: Map<Weapon, Int>): Int {
        var sum = 0
        purchaseWeapons.forEach { entry ->
            sum += entry.value * entry.key.price
        }
        return sum
    }

    fun calculateHarvesterPrice(count: Int): Int =
        (5 * 2.toDouble().pow(harvestersPurchased) * (2.toDouble().pow(count) - 1)).toInt()
    fun calculatePrices(purchases: PlayerStep): Int =
        calculateWeaponPrices(purchases.purchaseWeapons) +
            calculateHarvesterPrice(purchases.buildHarvesters.size)

    fun calculatePower(field: GameStateField): Int {
        var sum = getWeaponCount(field.effectiveWeapon) * 2
        if (field !in ownedFields) {
            sum += getWeaponCount(Weapon.LEGION) * 3
        }
        weapons.filter { weapon -> weapon.key != Weapon.LEGION }.keys.forEach { weapon ->
            sum += getWeaponCount(weapon)
        }
        return sum
    }

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

    fun loseWeaponPrecent(percent: Double) {
        val normalizedPercent = if (percent > 1.0) 1.0 else percent
        weapons.keys.forEach { weapon ->
            weapons[weapon] = ((weapons[weapon]?.toDouble() ?: 0.0) * (1 - normalizedPercent))
                .toInt()
        }
    }
}
