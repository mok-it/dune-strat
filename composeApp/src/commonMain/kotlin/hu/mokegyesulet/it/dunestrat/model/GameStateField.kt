package hu.mokegyesulet.it.dunestrat.model

data class GameStateField(
    val id: String,
    val water: Int,
    val spice: Int,
    val effectiveWeapon: Weapon,
    val harvester: Boolean,
    val neighbours: Set<GameStateField>,
)
