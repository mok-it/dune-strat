package hu.mokegyesulet.it.dunestrat.model

data class Field(
    val id: String,
    val water: Int,
    val spice: Int,
    val effectiveWeapon: Weapon,
    val harvester: Boolean,
    val neighbours: List<Field>,
)