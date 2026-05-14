package hu.mokegyesulet.it.dunestrat.feature.init

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.mokegyesulet.it.dunestrat.backend.SupabaseRepository
import hu.mokegyesulet.it.dunestrat.model.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class InitializationPhase {
    STARTING_CONDITIONS,
    TEAMS,
}

class InitViewModel() : ViewModel() {

    private fun createDefaultPlayer(index: Int) = Player(
        id = index,
        water = 0,
        spice = 10,
        weapons = mutableMapOf(
            Weapon.PISTOL to 0,
            Weapon.LASGUN to 0,
            Weapon.CRYSKNIFE to 0,
            Weapon.LEGION to 0,
        ),
        ownedFields = mutableSetOf(),
        harvestersPurchased = 0,
    )

    val currentPhase = mutableStateOf(InitializationPhase.STARTING_CONDITIONS)
    val basePlayerState: MutableState<Player> = mutableStateOf(createDefaultPlayer(-1))
    val playerList: MutableState<List<Player>>
    val startingFieldIds: MutableState<List<String>>
    val teams: MutableState<List<Team>> = mutableStateOf(emptyList())

    private val _navigationEvent = MutableSharedFlow<Unit>()
    val navigationEvent: SharedFlow<Unit> = _navigationEvent.asSharedFlow()

    init {
        val startingPlayers = mutableListOf<Player>()
        val emptyFieldIds = mutableListOf<String>()
        repeat(12) { index ->
            startingPlayers.add(createDefaultPlayer(index))
            emptyFieldIds.add("")
        }
        playerList = mutableStateOf(startingPlayers)
        startingFieldIds = mutableStateOf(emptyFieldIds)
    }

    var dropdownExpanded = mutableStateOf(false)
    val playerCount = mutableStateOf(12)
    val mapOptions = SupabaseRepository.getDeserts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val isFormValid = mutableStateOf(false)

    val selectedDesert: MutableState<Desert?> = mutableStateOf(null)

    fun onEvent(event: InitScreenEvent) {
        when (event) {
            is InitScreenEvent.ChangeMapDropdownExpanded -> {
                dropdownExpanded.value = event.expanded
            }

            is InitScreenEvent.ChangeSelectedMap -> {
                selectedDesert.value = event.selectedDesert

                val startingFields = event.selectedDesert.fields.filter { it.startingField }
                playerCount.value = startingFields.size
                dropdownExpanded.value = false
                resizePlayerList(playerCount.value)

                startingFieldIds.value = startingFields.map { it.id }
                validateForm()
            }

            is InitScreenEvent.UpdateGlobalStartingConditions -> {
                basePlayerState.value = event.playerData
                validateForm()
            }

            is InitScreenEvent.UpdatePlayerStartingField -> {
                val newStartingFieldIds = startingFieldIds.value.toMutableList()
                newStartingFieldIds[event.index] = event.fieldId
                startingFieldIds.value = newStartingFieldIds
                validateForm()
            }

            is InitScreenEvent.ProceedToTeams -> {
                if (teams.value.isEmpty() || teams.value.size != playerList.value.size) {
                    teams.value = playerList.value.map { player ->
                        Team(player.id, listOf(Student("")))
                    }
                }
                currentPhase.value = InitializationPhase.TEAMS
                validateForm()
            }

            is InitScreenEvent.BackToConditions -> {
                currentPhase.value = InitializationPhase.STARTING_CONDITIONS
                validateForm()
            }

            is InitScreenEvent.AddStudent -> {
                val newTeams = teams.value.toMutableList()
                val team = newTeams[event.teamIndex]
                val newStudents = team.students.toMutableList()
                newStudents.add(Student(""))
                newTeams[event.teamIndex] = Team(team.playerId, newStudents)
                teams.value = newTeams
                validateForm()
            }

            is InitScreenEvent.RemoveStudent -> {
                val newTeams = teams.value.toMutableList()
                val team = newTeams[event.teamIndex]
                if (team.students.size > 1) {
                    val newStudents = team.students.toMutableList()
                    newStudents.removeAt(event.studentIndex)
                    newTeams[event.teamIndex] = Team(team.playerId, newStudents)
                    teams.value = newTeams
                    validateForm()
                }
            }

            is InitScreenEvent.UpdateStudent -> {
                val newTeams = teams.value.toMutableList()
                val team = newTeams[event.teamIndex]
                val newStudents = team.students.toMutableList()
                newStudents[event.studentIndex] = event.student
                newTeams[event.teamIndex] = Team(team.playerId, newStudents)
                teams.value = newTeams
                validateForm()
            }

            is InitScreenEvent.InitPlayerOnMap -> {}
            is InitScreenEvent.UpdatePlayerData -> {}
        }
    }

    private fun validateForm() {
        if (currentPhase.value == InitializationPhase.STARTING_CONDITIONS) {
            val desertSelected = selectedDesert.value != null
            val allFieldsAssigned = startingFieldIds.value.all { it.isNotBlank() }
            val uniqueFields = startingFieldIds.value.distinct().size == startingFieldIds.value.size

            val basePlayer = basePlayerState.value
            val baseConditionsValid = basePlayer.water >= 0 &&
                basePlayer.spice >= 0 &&
                Weapon.entries.all { basePlayer.getWeaponCount(it) >= 0 }

            isFormValid.value =
                desertSelected &&
                allFieldsAssigned &&
                uniqueFields &&
                baseConditionsValid
        } else {
            isFormValid.value = teams.value.all { team ->
                team.students.all { student -> student.name.isNotBlank() }
            }
        }
    }

    private fun resizePlayerList(newCount: Int) {
        val currentList = playerList.value.toMutableList()
        val currentFields = startingFieldIds.value.toMutableList()

        if (currentList.size > newCount) {
            playerList.value = currentList.take(newCount)
            startingFieldIds.value = currentFields.take(newCount)
        } else {
            repeat(newCount - currentList.size) { offset ->
                playerList.value += createDefaultPlayer(currentList.size + offset)
                startingFieldIds.value += ""
            }
        }
        playerCount.value = newCount
        teams.value = emptyList() // Reset teams when count changes
        validateForm()
    }

    fun savePlayers() {
        val desert = selectedDesert.value ?: return
        val base = basePlayerState.value
        val finalTeams = teams.value

        viewModelScope.launch {
            println("Játékosok mentése...")

            val gameTemplate = Game(
                name = "Játék - ${desert.name}",
                progress = GameProgress.INITIALIZED,
                teams = finalTeams,
                desertId = desert.id,
            )
            val savedGame = SupabaseRepository.saveGame(gameTemplate)

            val stateFields = desert.fields.toGameStateFields()

            val initialPlayers = playerList.value.mapIndexed { index, player ->
                val fieldId = startingFieldIds.value[index]
                player.copy(
                    water = base.water,
                    spice = base.spice,
                    weapons = Weapon.entries.associateWith {
                        base.getWeaponCount(it)
                    }.toMutableMap(),
                    ownedFields = mutableSetOf(stateFields.first { it.id == fieldId }),
                )
            }

            val firstState = GameState(
                id = -1,
                gameId = savedGame.id,
                index = 0,
                fields = stateFields,
                players = initialPlayers.toSet(),
            )
            SupabaseRepository.saveGameState(firstState)

            println("Játék és kezdeti állapot sikeresen mentve! Game ID: ${savedGame.id}")
            _navigationEvent.emit(Unit)
        }
    }

    sealed class InitScreenEvent {
        data object InitPlayerOnMap : InitScreenEvent()
        data class ChangeMapDropdownExpanded(val expanded: Boolean) : InitScreenEvent()
        data class ChangeSelectedMap(val selectedDesert: Desert) : InitScreenEvent()
        data class UpdateGlobalStartingConditions(val playerData: Player) : InitScreenEvent()
        data class UpdatePlayerData(val playerData: Player, val index: Int) : InitScreenEvent()
        data class UpdatePlayerStartingField(
            val fieldId: String,
            val index: Int,
        ) : InitScreenEvent()

        data object ProceedToTeams : InitScreenEvent()
        data object BackToConditions : InitScreenEvent()
        data class AddStudent(val teamIndex: Int) : InitScreenEvent()
        data class RemoveStudent(val teamIndex: Int, val studentIndex: Int) : InitScreenEvent()
        data class UpdateStudent(
            val teamIndex: Int,
            val studentIndex: Int,
            val student: Student,
        ) : InitScreenEvent()
    }
}
