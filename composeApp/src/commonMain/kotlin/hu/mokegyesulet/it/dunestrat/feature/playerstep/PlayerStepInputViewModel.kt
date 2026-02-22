package hu.mokegyesulet.it.dunestrat.feature.playerstep

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.mokegyesulet.it.dunestrat.backend.SupabaseRepository
import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.model.GameState
import hu.mokegyesulet.it.dunestrat.model.PlayerStep
import hu.mokegyesulet.it.dunestrat.model.Team
import hu.mokegyesulet.it.dunestrat.model.Weapon
import kotlin.collections.emptyList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn

class PlayerStepInputViewModel(
    val gameId: Int,
) : ViewModel() {

    val tabIndex = mutableStateOf(0)
    val game = SupabaseRepository.getGames().mapNotNull { list ->
        list.find { it.id == 0 }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        Game(
            name = "",
            teams = listOf(Team("", emptyList())),
            desertId = -1,
        ),
    )
    val gameState = SupabaseRepository.getGameStates().mapNotNull { list ->
        list.find { it.id == 0 }
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        GameState(
            id = -1,
            gameId = -1,
            players = emptySet(),
            fields = emptySet(),
        ),
    )
//    val game = mutableStateOf(
//        Game(
//            name = "Teszt",
//            progress = GameProgress.ONGOING,
//            teams = (1..12).map {
//                Team(
//                    playerId = it.toString(),
//                    students = listOf(
//                        Student(
//                            it.toString() + "a",
//                        ),
//                        Student(
//                            it.toString() + "b",
//                        ),
//                        Student(
//                            it.toString() + "c",
//                        ),
//                    ),
//                )
//            },
//        ),
//    )

    // Expose members as a stored derived State (not a getter) so Compose tracks it properly
    val members = derivedStateOf {
        game.value.teams.getOrNull(tabIndex.value)?.students?.joinToString { it.name } ?: ""
    }

    val uiStates = mutableStateListOf(
        EnterStepsUIState(),
        EnterStepsUIState(),
        EnterStepsUIState(),
        EnterStepsUIState(),
        EnterStepsUIState(),
        EnterStepsUIState(),
        EnterStepsUIState(),
        EnterStepsUIState(),
        EnterStepsUIState(),
        EnterStepsUIState(),
        EnterStepsUIState(),
        EnterStepsUIState(),
    )

    // Expose uiState as a stored derived State so consumers observe changes to uiStates and tabIndex
    val uiState = derivedStateOf {
        uiStates.getOrNull(tabIndex.value) ?: EnterStepsUIState()
    }

    // Compose consumers often need to observe list mutations directly. Return the SnapshotStateList
    // for the current tab so composables can read it and be subscribed to its changes.
    val leaveFields = derivedStateOf {
        uiStates.getOrNull(tabIndex.value)?.leaveFields ?: mutableStateListOf(Pair("", null))
    }

    val enterFields = derivedStateOf {
        uiStates.getOrNull(tabIndex.value)?.enterFields ?: mutableStateListOf(Pair("", null))
    }

    val purchaseWeapons = derivedStateOf {
        uiStates.getOrNull(tabIndex.value)?.purchaseWeapons ?: mapOf()
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.TabSelected -> tabIndex.value = event.index

            is Event.Save -> {
                val playerSteps = uiStates.mapIndexed { index, state ->
                    PlayerStep()
                }
            }

            is Event.LeaveField -> {
                val lf = uiStates[tabIndex.value].leaveFields
                val gs = gameState.value
                val value = event.value.toUpperCase(locale = Locale.current).filter {
                    it.isUpperCase() || it.isDigit()
                }
                val validation = when {
                    value == "" -> null

                    !gs.fields.map { it.id }.contains(event.value) -> Validation.NO_SUCH_FIELD

                    !gs.players.find {
                        it.id.toInt() == tabIndex.value + 1
                    }!!.ownedFields.map { it.id }.contains(event.value) -> Validation.UNOWNED_FIELD

                    else -> null
                }
                lf[event.index] =
                    value to validation
                if (!lf.contains("" to null)) {
                    lf.add("" to null)
                }

                println("size: ${lf.size}")
                for (i in lf) {
                    println(i)
                }
            }

            is Event.EnterField -> {
                val ef = uiStates[tabIndex.value].enterFields
                val gs = gameState.value
                val value = event.value.toUpperCase(locale = Locale.current).filter {
                    it.isUpperCase() || it.isDigit()
                }
                val validation = when {
                    value == "" -> null

                    !gs.fields.map { it.id }.contains(event.value) -> Validation.NO_SUCH_FIELD

                    gs.fields.find {
                        it.id == event.value
                    }!!.neighbours.map { it.id }.any { x: String ->
                        gs.players.find { it.id.toInt() == tabIndex.value + 1 }!!
                            .ownedFields.map { it.id }.contains(x)
                    } -> Validation.UNREACHABLE_FIELD

                    else -> null
                }
                ef[event.index] =
                    value to validation
                if (!ef.contains("" to null)) {
                    ef.add("" to null)
                }

                println("size: ${ef.size}")
                for (i in ef) {
                    println(i)
                }
            }

            is Event.PurchaseWeapon -> {
                if (event.value == "") {
                    uiStates[tabIndex.value].purchaseWeapons[event.weapon] = 0
                }
                try {
                    val amount = event.value.toInt()
                    if (amount >= 0) {
                        uiStates[tabIndex.value].purchaseWeapons[event.weapon] = amount
                    }
                } catch (_: NumberFormatException) {}
            }

            is Event.PurchaseHarvester -> {
                val gameState = gameState.value
                val value = event.value.toUpperCase(locale = Locale.current).filter {
                    it.isUpperCase() || it.isDigit()
                }
                val validation = when {
                    value == "" -> null

                    !gameState.fields.map {
                        it.id
                    }.contains(event.value) -> Validation.NO_SUCH_FIELD

                    !gameState.players.find {
                        it.id.toInt() == tabIndex.value + 1
                    }!!.ownedFields.map { it.id }.contains(event.value) -> Validation.UNOWNED_FIELD

                    else -> null
                }

                uiStates[tabIndex.value].purchaseHarvester.value = value to validation
            }
        }
    }

    data class EnterStepsUIState(
        val leaveFields: SnapshotStateList<Pair<String, Validation?>> =
            mutableStateListOf(Pair("", null)),
        val enterFields: SnapshotStateList<Pair<String, Validation?>> =
            mutableStateListOf(Pair("", null)),
        val purchaseWeapons: MutableMap<Weapon, Int> = mutableStateMapOf(
            Weapon.CRYSKNIFE to 0,
            Weapon.PISTOL to 0,
            Weapon.LASGUN to 0,
            Weapon.LEGION to 0,
        ),
        val purchaseHarvester: MutableState<Pair<String, Validation?>> =
            mutableStateOf("" to null),
    )

    enum class Validation(val message: String, val isError: Boolean) {
        NO_SUCH_FIELD("Ilyen mező nincs a pályán!", false),
        UNREACHABLE_FIELD("A játékos nem birtokol szomszédos mezőt!", true),
        UNOWNED_FIELD("A játékos nem birtokolja a mezőt!", true),
    }

    sealed class Event {
        data class TabSelected(val index: Int) : Event()
        data object Save : Event()
        data class LeaveField(val index: Int, val value: String) : Event()
        data class EnterField(val index: Int, val value: String) : Event()
        data class PurchaseWeapon(val weapon: Weapon, val value: String) : Event()
        data class PurchaseHarvester(val value: String) : Event()
    }
}
