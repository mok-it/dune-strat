package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Desert(
    @Transient
    val id: Int? = null,
    val fields: Set<DesertField>,
) {
    companion object {
        fun create12PlayerHexagon(): Desert {
            TODO()
        }
    }
}
