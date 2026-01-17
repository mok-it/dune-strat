package hu.mokegyesulet.it.dunestrat.backend

import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.model.GameState
import hu.mokegyesulet.it.dunestrat.model.PlayerStep
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object MemoryRepository : Repository {
    private val game = MutableStateFlow<List<Game>>(emptyList())
    private val gameState = MutableStateFlow<List<GameState>>(emptyList())
    private val playerStep = MutableStateFlow<List<PlayerStep>>(emptyList())

    override fun getGames(): Flow<List<Game>> = game.asStateFlow()

    override suspend fun saveGame(game: Game) {
        this.game.value += game
    }

    override fun getGameStates(): Flow<List<GameState>> = gameState.asStateFlow()

    override suspend fun saveGameState(gameState: GameState) {
        this.gameState.value += gameState
    }

    override fun getPlayerSteps(): Flow<List<PlayerStep>> = playerStep.asStateFlow()

    override suspend fun savePlayerStep(playerStep: PlayerStep) {
        this.playerStep.value += playerStep
    }
}
