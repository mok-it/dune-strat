package hu.mokegyesulet.it.dunestrat.backend

import hu.mokegyesulet.it.dunestrat.model.Desert
import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.model.GameState
import hu.mokegyesulet.it.dunestrat.model.PlayerStep
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun saveGame(game: Game)
    fun getGames(): Flow<List<Game>>

    suspend fun saveGameState(gameState: GameState)
    fun getGameStates(): Flow<List<GameState>>
    fun getLatestGameStateByGameId(gameId: String): Flow<GameState>

    suspend fun savePlayerStep(playerStep: PlayerStep)
    fun getPlayerSteps(): Flow<List<PlayerStep>>
    fun getPlayerStepsByGameStateId(gameStateId: String): Flow<List<PlayerStep>>

    suspend fun saveDesert(desert: Desert)
    fun getDeserts(): Flow<List<Desert>>
}
