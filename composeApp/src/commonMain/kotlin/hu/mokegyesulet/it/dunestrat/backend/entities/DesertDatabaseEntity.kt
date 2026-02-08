package hu.mokegyesulet.it.dunestrat.backend.entities

import hu.mokegyesulet.it.dunestrat.model.Desert
import hu.mokegyesulet.it.dunestrat.model.DesertField
import hu.mokegyesulet.it.dunestrat.model.Weapon
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
class DesertDatabaseEntity(
    @EncodeDefault(EncodeDefault.Mode.NEVER)
    val id: Int = -1,
    val name: String,
    @SerialName("field_jsons")
    val fieldJsons: List<String>,
) {
    fun toDesert(): Desert {
        val fieldsWithIdList = fieldJsons.map { Json.decodeFromString<DesertFieldWithIdList>(it) }

        val fields = mutableMapOf<String, DesertField>()
        fieldsWithIdList.forEach { fieldWithIdList ->
            fields[fieldWithIdList.id] = fieldWithIdList.toDesertField()
        }

        fieldsWithIdList.forEach { fieldWithIdList ->
            val field = fields[fieldWithIdList.id]!!
            fieldWithIdList.neighboursId.forEach { neighbourId ->
                val neighbourField = fields[neighbourId]!!
                field.neighbours.add(neighbourField)
            }
        }

        return Desert(
            id = id,
            name = name,
            fields = fields.values.toSet(),
        )
    }

    companion object {
        const val TABLE_NAME = "desert"
    }
}

fun Desert.toDatabaseEntity() = DesertDatabaseEntity(
    id = id,
    name = name,
    fieldJsons = fields.map { Json.encodeToString(it.toDesertFieldWithIdList()) },
)

@Serializable
class DesertFieldWithIdList(
    val id: String,
    val water: Int,
    val spice: Int,
    val effectiveWeapon: Weapon,
    val neighboursId: List<String>,
    val startingField: Boolean,
) {
    fun toDesertField() = DesertField(
        id = id,
        water = water,
        spice = spice,
        effectiveWeapon = effectiveWeapon,
        neighbours = mutableSetOf(),
        startingField = startingField,
    )
}

fun DesertField.toDesertFieldWithIdList() = DesertFieldWithIdList(
    id = id,
    water = water,
    spice = spice,
    effectiveWeapon = effectiveWeapon,
    neighboursId = neighbours.map { it.id },
    startingField = startingField,
)
