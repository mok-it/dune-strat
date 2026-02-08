package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable

@Serializable
class GameStateField(
    val id: String,
    val water: Int,
    val spice: Int,
    val effectiveWeapon: Weapon,
    var harvester: Boolean,
    val neighbours: MutableSet<GameStateField>,
) {
    fun copy(
        id: String = this.id,
        water: Int = this.water,
        spice: Int = this.spice,
        effectiveWeapon: Weapon = this.effectiveWeapon,
        harvester: Boolean = this.harvester,
        neighbours: Set<GameStateField> = this.neighbours,
    ): GameStateField = GameStateField(
        id,
        water,
        spice,
        effectiveWeapon,
        harvester,
        neighbours.toMutableSet(),
    )
}
fun Set<DesertField>.toGameStateFields(): Set<GameStateField> {
    val gameStateFields = this.map {
        GameStateField(
            it.id,
            it.water,
            it.spice,
            it.effectiveWeapon,
            false,
            mutableSetOf(),
        )
    }.toSet()
    for (stateField in gameStateFields) {
        val neighbors =
            this
                .first { it.id == stateField.id }
                .neighbours
                .map { neighbor -> gameStateFields.first({ it.id == neighbor.id }) }
                .toSet()
        stateField.neighbours.addAll(neighbors)
    }
    return gameStateFields
}
