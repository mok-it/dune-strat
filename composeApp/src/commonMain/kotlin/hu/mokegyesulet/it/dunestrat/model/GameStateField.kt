package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable

@Serializable
data class GameStateField(
    val id: String,
    val water: Int,
    val spice: Int,
    val effectiveWeapon: Weapon,
    var harvester: Boolean,
    val neighbours: MutableSet<GameStateField>,
)
