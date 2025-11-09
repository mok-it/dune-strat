package hu.mokegyesulet.it.dunestrat.model

data class PlayerStep(
    val playerId: String,
    val leaveFields: Set<GameStateField>,
    val enterFields: Set<GameStateField>,
    val purchaseWeapons: Map<Weapon, Int>,
    val buildHarvester: Set<GameStateField>,
)
