package hu.mokegyesulet.it.dunestrat.model

class PlayerStep(
    val playerId: String,
    var leaveFields: Set<GameStateField>,
    var enterFields: Set<GameStateField>,
    var purchaseWeapons: Map<Weapon, Int>,
    var buildHarvester: Set<GameStateField>,
)
