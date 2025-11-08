package hu.mokegyesulet.it.dunestrat.model

data class PlayerStep(
    val player: Player,
    val leaveFields: Set<Field>,
    val enterFields: Set<Field>,
    val purchaseWeapons: Map<Weapon, Int>,
    val buildHarvester: Set<Field>,
)
