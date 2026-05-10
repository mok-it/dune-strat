package hu.mokegyesulet.it.dunestrat.feature.init

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.mokegyesulet.it.dunestrat.backend.SupabaseRepository
import hu.mokegyesulet.it.dunestrat.model.Desert
import hu.mokegyesulet.it.dunestrat.model.Player
import hu.mokegyesulet.it.dunestrat.model.Weapon
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

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

    val playerList: MutableState<List<Player>>
    val startingFieldIds: MutableState<List<String>>

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

            is InitScreenEvent.UpdatePlayerData -> {
                val newPlayerList = playerList.value.toMutableList()
                newPlayerList[event.index] = event.playerData
                playerList.value = newPlayerList
                validateForm()
            }

            is InitScreenEvent.UpdatePlayerStartingField -> {
                val newStartingFieldIds = startingFieldIds.value.toMutableList()
                newStartingFieldIds[event.index] = event.fieldId
                startingFieldIds.value = newStartingFieldIds
                validateForm()
            }

            is InitScreenEvent.InitPlayerOnMap -> {}
        }
    }

    private fun validateForm() {
        // Minden mezőnek ki kell lennie töltve és a kezdő mezőknek egyedinek kell lenniük
        val allFieldsAssigned = startingFieldIds.value.all { it.isNotBlank() }
        val uniqueFields = startingFieldIds.value.distinct().size == startingFieldIds.value.size

        isFormValid.value = allFieldsAssigned && uniqueFields && playerList.value.all { player ->
            player.water >= 0 &&
                player.spice >= 0 &&
                Weapon.entries.all { player.getWeaponCount(it) >= 0 }
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
        validateForm()
    }

    fun savePlayers() {
        // TODO: implementálni a mentést
        println("Játékosok mentése...")
        playerList.value.forEachIndexed { index, player ->
            println("${index + 1}. Jatekos: $player, Kezdo mezo: ${startingFieldIds.value[index]}")
        }
    }

    sealed class InitScreenEvent {
        data object InitPlayerOnMap : InitScreenEvent()
        data class ChangeMapDropdownExpanded(val expanded: Boolean) : InitScreenEvent()
        data class ChangeSelectedMap(val selectedDesert: Desert) : InitScreenEvent()
        data class UpdatePlayerData(val playerData: Player, val index: Int) : InitScreenEvent()
        data class UpdatePlayerStartingField(
            val fieldId: String,
            val index: Int,
        ) : InitScreenEvent()
    }
}
