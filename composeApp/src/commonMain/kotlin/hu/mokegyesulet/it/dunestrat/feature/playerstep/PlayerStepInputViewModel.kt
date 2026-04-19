package hu.mokegyesulet.it.dunestrat.feature.playerstep

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.mokegyesulet.it.dunestrat.backend.SupabaseRepository
import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.model.GameState
import hu.mokegyesulet.it.dunestrat.model.PlayerStep
import hu.mokegyesulet.it.dunestrat.model.Weapon
import kotlinx.coroutines.launch

class PlayerStepInputViewModel(
    val gameId: Int,
) : ViewModel() {

    val tabIndex = mutableStateOf(0)

    val game = mutableStateOf<Game?>(null)
    val gameState = mutableStateOf<GameState?>(null)
    val playerSteps = mutableStateOf(listOf<PlayerStep>())

    val loaded = derivedStateOf {
        game.value != null && gameState.value != null && playerSteps.value.isNotEmpty()
    }

    init {
        viewModelScope.launch {
            game.value = SupabaseRepository.getGameById(gameId)
            gameState.value = SupabaseRepository.getLatestGameStateByGameId(gameId)
            val playerStepFlow = SupabaseRepository.getPlayerStepsByGameStateId(
                gameState.value!!.id,
            )
            playerStepFlow.collect { list ->
                playerSteps.value = list.ifEmpty {
                    game.value!!.teams.map {
                        PlayerStep(
                            gameStateId = gameState.value!!.id,
                            playerId = it.playerId,
                            leaveFields = emptySet(),
                            enterFields = emptySet(),
                            purchaseWeapons = emptyMap(),
                            buildHarvesters = emptySet(),
                        )
                    }
                }
            }
        }
    }

    // Expose members as a stored derived State (not a getter) so Compose tracks it properly
    val members = derivedStateOf {
        game.value?.teams?.getOrNull(tabIndex.value)?.students?.joinToString { it.name } ?: ""
    }

    val uiStates = derivedStateOf {
        playerSteps.value.map { playerStep ->

            val purchaseWeapons = mutableStateMapOf<Weapon, Int>()
            purchaseWeapons.putAll(playerStep.purchaseWeapons)

            EnterStepsUIState(
                stepId = playerStep.id,
                playerId = playerStep.playerId,
                leaveFields = (playerStep.leaveFields + "").map {
                    it to null
                }.toMutableStateList(),
                enterFields = (playerStep.enterFields + "").map {
                    it to null
                }.toMutableStateList(),
                purchaseWeapons = purchaseWeapons,
                purchaseHarvester = mutableStateOf(
                    (playerStep.buildHarvesters.firstOrNull() ?: "") to null,
                ),
            )
        }
    }

    // Expose uiState as a stored derived State so consumers observe changes to uiStates and tabIndex
    val uiState = derivedStateOf {
        uiStates.value.getOrNull(tabIndex.value) ?: EnterStepsUIState()
    }

    // Compose consumers often need to observe list mutations directly. Return the SnapshotStateList
    // for the current tab so composables can read it and be subscribed to its changes.
    val leaveFields = derivedStateOf {
        uiStates.value.getOrNull(tabIndex.value)?.leaveFields ?: mutableStateListOf(Pair("", null))
    }

    val enterFields = derivedStateOf {
        uiStates.value.getOrNull(tabIndex.value)?.enterFields ?: mutableStateListOf(Pair("", null))
    }

    val purchaseWeapons = derivedStateOf {
        uiStates.value.getOrNull(tabIndex.value)?.purchaseWeapons ?: mutableStateMapOf()
    }

    fun onEvent(event: Event) {
        if (!loaded.value) {
            return
        }
        val gameState =
            gameState.value ?: throw IllegalStateException("Game state is not loaded yet!")
        when (event) {
            is Event.TabSelected -> tabIndex.value = event.index

            is Event.RunTurn -> {
                viewModelScope.launch {
                    val playerSteps = getPlayerSteps()
                    val newGameState = gameState.runTurn(playerSteps)
                    SupabaseRepository.saveGameState(newGameState)
                }
            }

            is Event.LeaveField -> {
                println(event.value)

                val lf = uiStates.value[tabIndex.value].leaveFields
                val fieldId = event.value.toFieldId()
                val validation = when {
                    fieldId == "" -> null

                    !gameState.fields.map {
                        it.id
                    }.contains(fieldId) -> Validation.NO_SUCH_FIELD

                    !isFieldOwnedByPlayer(
                        fieldId,
                        uiStates.value[tabIndex.value].playerId,
                    ) -> Validation.UNOWNED_FIELD

                    else -> null
                }
                lf[event.index] =
                    fieldId to validation
                if (!lf.contains("" to null)) {
                    lf.add("" to null)
                }
            }

            is Event.EnterField -> {
                val ef = uiStates.value[tabIndex.value].enterFields
                val fieldId = event.value.toFieldId()
                val validation = when {
                    fieldId == "" -> null

                    !isFieldIdInGame(fieldId) -> Validation.NO_SUCH_FIELD

                    !isFieldReachableByPlayer(
                        fieldId,
                        uiStates.value[tabIndex.value].playerId,
                    ) -> Validation.UNREACHABLE_FIELD

                    else -> null
                }
                ef[event.index] =
                    fieldId to validation
                if (!ef.contains("" to null)) {
                    ef.add("" to null)
                }
            }

            is Event.PurchaseWeapon -> {
                if (event.value == "") {
                    uiStates.value[tabIndex.value].purchaseWeapons[event.weapon] = 0
                }
                try {
                    val amount = event.value.toInt()
                    if (amount >= 0) {
                        uiStates.value[tabIndex.value].purchaseWeapons[event.weapon] = amount
                    }
                } catch (_: NumberFormatException) {}
            }

            is Event.PurchaseHarvester -> {
                val fieldId = event.value.toFieldId()
                val validation = when {
                    fieldId == "" -> null

                    !isFieldIdInGame(fieldId) -> Validation.NO_SUCH_FIELD

                    !isFieldOwnedByPlayer(
                        fieldId,
                        uiStates.value[tabIndex.value].playerId,
                    ) -> Validation.UNOWNED_FIELD

                    else -> null
                }

                uiStates.value[tabIndex.value].purchaseHarvester.value = fieldId to validation
            }

            is Event.SaveToDatabase -> {
                viewModelScope.launch {
                    val playerStep = uiStates.value[tabIndex.value].toPlayerStep(gameState)
                    if (playerStep.gameStateId != -1) {
                        SupabaseRepository.savePlayerStep(playerStep)
                    }
                }
            }
        }
    }

    private fun getPlayerSteps(): MutableSet<PlayerStep> {
        val gameState =
            gameState.value ?: throw IllegalStateException("Game state is not loaded yet!")
        return uiStates.value.map {
            it.toPlayerStep(gameState)
        }.toMutableSet()
    }

    private fun isFieldIdInGame(fieldId: String): Boolean {
        val gameState =
            gameState.value ?: throw IllegalStateException("Game state is not loaded yet!")
        return gameState.fields.map { it.id }.contains(fieldId)
    }

    private fun isFieldOwnedByPlayer(
        fieldId: String,
        playerId: String,
    ): Boolean {
        val gameState =
            gameState.value ?: throw IllegalStateException("Game state is not loaded yet!")
        val player = gameState.players.find { it.id == playerId }
        if (player == null) {
            return false
        }
        return player.ownedFields.map { it.id }.contains(fieldId)
    }

    private fun isFieldReachableByPlayer(
        fieldId: String,
        playerId: String,
    ): Boolean {
        val gameState =
            gameState.value ?: throw IllegalStateException("Game state is not loaded yet!")
        val player = gameState.players.find { it.id == playerId }
        if (player == null) {
            return false
        }
        return player.ownedFields.flatMap { it.neighbours }.map { it.id }.contains(fieldId)
    }

    private fun String.toFieldId(): String = this.toUpperCase(locale = Locale.current).filter {
        it.isUpperCase() || it.isDigit()
    }

    data class EnterStepsUIState(
        val stepId: Int = -1, // TODO: find latest player step here
        val playerId: String = "",
        val leaveFields: SnapshotStateList<Pair<String, Validation?>> =
            mutableStateListOf(Pair("", null)),
        val enterFields: SnapshotStateList<Pair<String, Validation?>> =
            mutableStateListOf(Pair("", null)),
        val purchaseWeapons: SnapshotStateMap<Weapon, Int> = mutableStateMapOf(
            Weapon.CRYSKNIFE to 0,
            Weapon.PISTOL to 0,
            Weapon.LASGUN to 0,
            Weapon.LEGION to 0,
        ),
        val purchaseHarvester: MutableState<Pair<String, Validation?>> =
            mutableStateOf("" to null),
    ) {
        fun toPlayerStep(gameState: GameState): PlayerStep = PlayerStep(
            id = stepId,
            gameStateId = gameState.id,
            playerId = playerId,
            leaveFields = leaveFields.map { it.first }
                .toSet(),
            enterFields = enterFields.map { it.first }
                .toSet(),
            purchaseWeapons = purchaseWeapons,
            buildHarvesters = if (purchaseHarvester.value.first !=
                ""
            ) {
                setOf(purchaseHarvester.value.first)
            } else {
                setOf()
            },
        )
    }

    enum class Validation(val message: String, val isError: Boolean) {
        NO_SUCH_FIELD("Ilyen mező nincs a pályán!", false),
        UNREACHABLE_FIELD("A játékos nem birtokol szomszédos mezőt!", true),
        UNOWNED_FIELD("A játékos nem birtokolja a mezőt!", true),
    }

    sealed class Event {
        data class TabSelected(val index: Int) : Event()
        data object RunTurn : Event()
        data class LeaveField(val index: Int, val value: String) : Event()
        data class EnterField(val index: Int, val value: String) : Event()
        data class PurchaseWeapon(val weapon: Weapon, val value: String) : Event()
        data class PurchaseHarvester(val value: String) : Event()
        data object SaveToDatabase : Event()
    }
}
