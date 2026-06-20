package hu.mokegyesulet.it.dunestrat.backend

import hu.mokegyesulet.it.dunestrat.model.Desert
import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.model.GameState
import hu.mokegyesulet.it.dunestrat.model.PlayerStep
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun saveGame(game: Game): Game
    fun getGames(): Flow<List<Game>>
    suspend fun getGameById(gameId: Int): Game

    suspend fun saveGameState(gameState: GameState)
    fun getGameStates(): Flow<List<GameState>>
    suspend fun getLatestGameStateByGameId(gameId: Int): GameState

    suspend fun savePlayerStep(playerStep: PlayerStep): PlayerStep
    fun getPlayerSteps(): Flow<List<PlayerStep>>
    fun getPlayerStepsByGameStateId(gameStateId: Int): Flow<List<PlayerStep>>
    fun getPlayerStep(
        gameStateId: Int,
        playerId: Int,
    ): Flow<PlayerStep>

    suspend fun saveDesert(desert: Desert): Desert
    fun getDeserts(): Flow<List<Desert>>

    suspend fun uploadImage(
        svg: String,
        gameName: String,
        gameId: String,
        round: Int,
    )
}
