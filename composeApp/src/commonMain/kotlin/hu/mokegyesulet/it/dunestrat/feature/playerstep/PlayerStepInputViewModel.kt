package hu.mokegyesulet.it.dunestrat.feature.playerstep

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.mokegyesulet.it.dunestrat.backend.SupabaseRepository
import hu.mokegyesulet.it.dunestrat.model.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlayerStepInputViewModel(
    val gameId: Int,
) : ViewModel() {

    val tabIndex = mutableStateOf(0)
    val game = SupabaseRepository.getGames().mapNotNull { list ->
        list.find { it.id == gameId }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        Game(
            name = "",
            teams = listOf(Team("", emptyList())),
            desertId = -1,
        ),
    )
    val gameState = SupabaseRepository.getLatestGameStateByGameId(
        gameId,
    ).stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        GameState(
            gameId = -1,
            index = -1,
            players = emptySet(),
            fields = emptySet(),
        ),
    )

    // Expose members as a stored derived State (not a getter) so Compose tracks it properly
    val members = derivedStateOf {
        game.value.teams.getOrNull(tabIndex.value)?.students?.joinToString { it.name } ?: ""
    }

    val uiStates = mutableStateOf(
        (1..12).map {
            EnterStepsUIState(
                playerId = it.toString(),
            )
        },
    )

    private val _uiStates = SupabaseRepository.getPlayerStepsByGameStateId(
        gameState.value.id,
    ).map { list ->
        if (list.isEmpty()) {
//            gameState.value.players.map {
//                EnterStepsUIState(
//                    playerId = it.id,
//                )
//            }
            (1..12).map {
                EnterStepsUIState(
                    playerId = it.toString(),
                )
            }
        } else {
            list.map { step ->

                val purchaseWeapons = mutableStateMapOf<Weapon, Int>()
                purchaseWeapons.putAll(step.purchaseWeapons)

                EnterStepsUIState(
                    stepId = step.id,
                    playerId = step.playerId,
                    leaveFields = step.leaveFields.map { it to null }.toMutableStateList(),
                    enterFields = step.enterFields.map { it to null }.toMutableStateList(),
                    purchaseWeapons = purchaseWeapons,
                    purchaseHarvester = mutableStateOf(
                        if (step.buildHarvesters.isNotEmpty()) {
                            step.buildHarvesters.first() to null
                        } else {
                            "" to null
                        },
                    ),
                )
            }
        }
    }

    init {
        viewModelScope.launch {
            _uiStates.collect {
                uiStates.value = it
            }
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
        val gameState = gameState.value
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
        val gameState = gameState.value
        return uiStates.value.map {
            it.toPlayerStep(gameState)
        }.toMutableSet()
    }

    private fun isFieldIdInGame(fieldId: String): Boolean {
        val gameState = gameState.value
        return gameState.fields.map { it.id }.contains(fieldId)
    }

    private fun isFieldOwnedByPlayer(
        fieldId: String,
        playerId: String,
    ): Boolean {
        val gameState = gameState.value
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
        val gameState = gameState.value
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
        val stepId: Int = -1,
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
