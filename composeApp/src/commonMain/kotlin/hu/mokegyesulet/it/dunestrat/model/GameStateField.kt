package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = GameStateFieldSerializer::class)
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

@Serializable
private data class GameStateFieldSurrogate(
    val id: String,
    val water: Int,
    val spice: Int,
    val effectiveWeapon: Weapon,
    var harvester: Boolean,
    val neighbours: List<String>,
)

object GameStateFieldSerializer : KSerializer<GameStateField> {
    override val descriptor: SerialDescriptor = GameStateFieldSurrogate.serializer().descriptor

    override fun serialize(
        encoder: Encoder,
        value: GameStateField,
    ) {
        val surrogate = GameStateFieldSurrogate(
            id = value.id,
            water = value.water,
            spice = value.spice,
            effectiveWeapon = value.effectiveWeapon,
            harvester = value.harvester,
            neighbours = value.neighbours.map { it.id },
        )
        encoder.encodeSerializableValue(GameStateFieldSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): GameStateField {
        val surrogate = decoder.decodeSerializableValue(GameStateFieldSurrogate.serializer())
        return GameStateField(
            id = surrogate.id,
            water = surrogate.water,
            spice = surrogate.spice,
            effectiveWeapon = surrogate.effectiveWeapon,
            harvester = surrogate.harvester,
            neighbours = mutableSetOf(),
        )
    }
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
