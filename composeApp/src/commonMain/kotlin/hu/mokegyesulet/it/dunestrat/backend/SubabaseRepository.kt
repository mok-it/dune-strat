package hu.mokegyesulet.it.dunestrat.backend

import hu.mokegyesulet.it.dunestrat.backend.entities.GameDatabaseEntry
import hu.mokegyesulet.it.dunestrat.backend.entities.GameStateDatabaseEntry
import hu.mokegyesulet.it.dunestrat.backend.entities.PlayerStepDatabaseEntry
import hu.mokegyesulet.it.dunestrat.backend.entities.toDatabaseEntry
import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.model.GameState
import hu.mokegyesulet.it.dunestrat.model.PlayerStep
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object SubabaseRepository : Repository {
    val supabase = SupabaseClientProvider.client
    const val GAME_TABLE = GameDatabaseEntry.TABLE_NAME
    const val GAME_STATE_TABLE = GameDatabaseEntry.TABLE_NAME
    const val STEP_TABLE = PlayerStepDatabaseEntry.TABLE_NAME

    @OptIn(SupabaseExperimental::class)
    override fun getGames(): Flow<List<Game>> =
        supabase.from(GAME_TABLE).selectAsFlow(GameDatabaseEntry::id).map { list ->
            list.map { it.toGame() }
        }

    override suspend fun saveGame(game: Game) {
        supabase.from(GAME_TABLE).insert(game.toDatabaseEntry())
    }

    @OptIn(SupabaseExperimental::class)
    override fun getGameStates(): Flow<List<GameState>> =
        supabase.from(GAME_STATE_TABLE).selectAsFlow(GameStateDatabaseEntry::id).map { list ->
            list.map { it.toGameState() }
        }

    override suspend fun saveGameState(gameState: GameState) {
        supabase.from(GAME_STATE_TABLE).insert(gameState.toDatabaseEntry())
    }

    @OptIn(SupabaseExperimental::class)
    override fun getPlayerSteps(): Flow<List<PlayerStep>> =
        supabase.from(STEP_TABLE).selectAsFlow(PlayerStepDatabaseEntry::id).map { list ->
            list.map { it.toPlayerStep() }
        }

    override suspend fun savePlayerStep(playerStep: PlayerStep) {
        supabase.from(STEP_TABLE).insert(playerStep.toDatabaseEntry())
    }
}
