package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable

@Serializable
class DesertField(
    var id: String,
    var water: Int,
    var spice: Int,
    var effectiveWeapon: Weapon,
    val neighbours: MutableSet<DesertField>,
    var startingField: Boolean,
)
