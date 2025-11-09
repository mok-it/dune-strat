package hu.mokegyesulet.it.dunestrat.model

data class DesertField(
    val id: String,
    val water: Int,
    val spice: Int,
    val effectiveWeapon: Weapon,
    val neighbours: Set<DesertField>,
    val startingField: Boolean,
)
