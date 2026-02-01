package hu.mokegyesulet.it.dunestrat.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Game(
    @Transient
    val id: Int = -1,
    val name: String,
    @Transient
    val progress: GameProgress = GameProgress.ERROR,
    val teams: List<Team>,
    @Transient
    val desertId: Int = -1,
)

enum class GameProgress {
    INITIALIZED,
    ONGOING,
    FINISHED,
    ERROR,
}
