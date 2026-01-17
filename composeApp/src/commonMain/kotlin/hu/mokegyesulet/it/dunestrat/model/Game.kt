package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Game(
    @Transient
    val id: Int? = null,
    val name: String,
    @Transient
    val progress: GameProgress = GameProgress.ERROR,
    val teams: List<Team>,
)

enum class GameProgress {
    INITIALIZED,
    ONGOING,
    FINISHED,
    ERROR,
}
