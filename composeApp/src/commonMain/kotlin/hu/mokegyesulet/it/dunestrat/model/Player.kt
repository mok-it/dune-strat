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
        var purchasePrice = 0
        purchaseWeapons.keys.forEach { weapon ->
            purchasePrice += weapon.price
        }

        purchaseWeapons.forEach { weapon ->
            weapons[weapon.key] = getWeaponCount(weapon.key) + weapon.value
        }

        spice -= purchasePrice
    }

    fun deliverWeapons(purchaseWeapons: Map<Weapon, Int>) {
        purchaseWeapons.keys.forEach { weapon ->
            weapons[weapon] = getWeaponCount(weapon) + (purchaseWeapons[weapon] ?: 0)
        }
    }

    fun purchaseHarvester(purchaseField: GameStateField) {
        purchaseField.harvester = true
        harvestersPurchased++
        spice -= 5 * 2.toDouble().pow(harvestersPurchased).toInt()
    }

    fun calculatePrices(purchases: PlayerStep): MutableMap<String, Int> {
        val prices = mutableMapOf<String, Int>()
        var sum = 0
        purchases.purchaseWeapons.keys.forEach { weapon ->
            sum += weapon.price
        }
        prices["weapons"] = sum
        prices["harvester"] = 5 * 2.toDouble().pow(harvestersPurchased).toInt()
        return prices
    }

    fun validatePrices(purchasePrices: MutableMap<String, Int>): Boolean {
        var sum = 0
        purchasePrices.values.forEach { value -> sum += value }
        return sum >= spice
    }
}
