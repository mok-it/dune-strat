package hu.mokegyesulet.it.dunestrat

import hu.mokegyesulet.it.dunestrat.model.Player
import hu.mokegyesulet.it.dunestrat.model.PlayerStep
import hu.mokegyesulet.it.dunestrat.model.Weapon
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PlayerTest {
    @Test
    fun weaponTest1() {
        val player = Player(
            id = 2517,
            water = 200,
            spice = 200,
            harvestersPurchased = 0,
            weapons = mutableMapOf(Weapon.CRYSKNIFE to 1, Weapon.LASGUN to 1, Weapon.PISTOL to 1),
            ownedFields = mutableSetOf(),
        )
        val purchases = mapOf(Weapon.CRYSKNIFE to 2, Weapon.LASGUN to 2, Weapon.PISTOL to 2)
        val step = PlayerStep(
            playerId = player.id,
            purchaseWeapons = purchases,
        )
        assertTrue { player.spice > player.calculatePrices(step) }
        player.purchaseWeapons(step.purchaseWeapons)
        player.deliverWeapons(step.purchaseWeapons)
        player.loseWeaponPrecent(0.0)
        assertEquals(3, player.getWeaponCount(Weapon.CRYSKNIFE))
        assertEquals(3, player.getWeaponCount(Weapon.LASGUN))
        assertEquals(3, player.getWeaponCount(Weapon.PISTOL))
    }

    @Test
    fun weaponTest2() {
        val player = Player(
            id = 2517,
            water = 200,
            spice = 200,
            harvestersPurchased = 0,
            weapons = mutableMapOf(Weapon.CRYSKNIFE to 1, Weapon.LASGUN to 1, Weapon.PISTOL to 1),
            ownedFields = mutableSetOf(),
        )
        val purchases = mapOf(
            Weapon.CRYSKNIFE to 4,
            Weapon.LASGUN to 4,
            Weapon.PISTOL to 4,
            Weapon.LEGION to 3,
        )
        val step = PlayerStep(
            playerId = player.id,
            purchaseWeapons = purchases,
        )
        assertTrue { player.spice > player.calculatePrices(step) }
        player.purchaseWeapons(step.purchaseWeapons)
        player.deliverWeapons(step.purchaseWeapons)
        player.loseWeaponPrecent(0.0)
        assertEquals(5, player.getWeaponCount(Weapon.CRYSKNIFE))
        assertEquals(5, player.getWeaponCount(Weapon.LASGUN))
        assertEquals(5, player.getWeaponCount(Weapon.PISTOL))
        assertEquals(3, player.getWeaponCount(Weapon.LEGION))
    }

    @Test
    fun weaponTest3() {
        val player = Player(
            id = 2517,
            water = 200,
            spice = 200,
            harvestersPurchased = 0,
            weapons = mutableMapOf(Weapon.CRYSKNIFE to 3, Weapon.LASGUN to 1, Weapon.PISTOL to 0),
            ownedFields = mutableSetOf(),
        )
        val purchases = mapOf(
            Weapon.CRYSKNIFE to 2,
            Weapon.LASGUN to 2,
            Weapon.PISTOL to 3,
            Weapon.LEGION to 1,
        )
        val step = PlayerStep(
            playerId = player.id,
            purchaseWeapons = purchases,
        )
        assertTrue { player.spice > player.calculatePrices(step) }
        player.purchaseWeapons(step.purchaseWeapons)
        player.deliverWeapons(step.purchaseWeapons)
        player.loseWeaponPrecent(0.0)
        assertEquals(5, player.getWeaponCount(Weapon.CRYSKNIFE))
        assertEquals(3, player.getWeaponCount(Weapon.LASGUN))
        assertEquals(3, player.getWeaponCount(Weapon.PISTOL))
        assertEquals(1, player.getWeaponCount(Weapon.LEGION))
    }
}
