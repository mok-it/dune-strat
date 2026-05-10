package hu.mokegyesulet.it.dunestrat.backend

import hu.mokegyesulet.it.dunestrat.backend.entities.*
import hu.mokegyesulet.it.dunestrat.model.Desert
import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.model.GameState
import hu.mokegyesulet.it.dunestrat.model.PlayerStep
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object SupabaseRepository : Repository {
    val supabase = SupabaseClientProvider.client
    const val GAME_TABLE = GameDatabaseEntity.TABLE_NAME
    const val GAME_STATE_TABLE = GameStateDatabaseEntity.TABLE_NAME
    const val STEP_TABLE = PlayerStepDatabaseEntity.TABLE_NAME
    const val DESERT_TABLE = DesertDatabaseEntity.TABLE_NAME

    @OptIn(SupabaseExperimental::class)
    override fun getGames(): Flow<List<Game>> =
        supabase.from(GAME_TABLE).selectAsFlow(GameDatabaseEntity::id).map { list ->
            list.map { it.toGame() }
        }

    override suspend fun saveGame(game: Game): Game =
        supabase.from(GAME_TABLE).insert(game.toDatabaseEntry()) {
            select()
        }.decodeSingle<GameDatabaseEntity>().toGame()

    @OptIn(SupabaseExperimental::class)
    override fun getGameStates(): Flow<List<GameState>> =
        supabase.from(GAME_STATE_TABLE).selectAsFlow(GameStateDatabaseEntity::id).map { list ->
            list.map { it.toGameState() }
        }

    @OptIn(SupabaseExperimental::class)
    override fun getLatestGameStateByGameId(gameId: String): Flow<GameState> =
        supabase.from(GAME_STATE_TABLE).selectAsFlow(
            GameStateDatabaseEntity::id,
            filter = FilterOperation("game_id", FilterOperator.EQ, gameId),
        ).map { list ->
            list.map { it.toGameState() }.maxByOrNull { it.index }
                ?: throw IllegalStateException("No game state found for game id: $gameId")
        }

    override suspend fun saveGameState(gameState: GameState): GameState =
        supabase.from(GAME_STATE_TABLE).insert(gameState.toDatabaseEntry()) {
            select()
        }.decodeSingle<GameStateDatabaseEntity>().toGameState()

    @OptIn(SupabaseExperimental::class)
    override fun getPlayerSteps(): Flow<List<PlayerStep>> =
        supabase.from(STEP_TABLE).selectAsFlow(PlayerStepDatabaseEntity::id).map { list ->
            list.map { it.toPlayerStep() }
        }

    @OptIn(SupabaseExperimental::class)
    override fun getPlayerStepsByGameStateId(gameStateId: String): Flow<List<PlayerStep>> =
        supabase.from(STEP_TABLE).selectAsFlow(
            PlayerStepDatabaseEntity::id,
            filter = FilterOperation("game_state_id", FilterOperator.EQ, gameStateId),
        ).map { list ->
            list.map { it.toPlayerStep() }
        }

    override suspend fun savePlayerStep(playerStep: PlayerStep) {
        supabase.from(STEP_TABLE).insert(playerStep.toDatabaseEntry())
    }

    override suspend fun saveDesert(desert: Desert) {
        supabase.from(DESERT_TABLE).insert(desert.toDatabaseEntity())
    }

    @OptIn(SupabaseExperimental::class)
    override fun getDeserts(): Flow<List<Desert>> =
        supabase.from(DESERT_TABLE).selectAsFlow(DesertDatabaseEntity::id).map { list ->
            list.map { it.toDesert() }
        }
}
