package hu.mokegyesulet.it.dunestrat.feature.init

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hu.mokegyesulet.it.dunestrat.model.Player

class InitViewModel() : ViewModel() {

    private val startingPlayer = Player("", 0, 0, mapOf(), setOf())
    val playerList: MutableState<List<Player>>
    val startingFieldIds: MutableState<List<String>>

    init {
        val startingPlayers = mutableListOf<Player>()
        val emptyFieldIds = mutableListOf<String>()
        repeat(12) {
            startingPlayers.add(startingPlayer.copy())
            emptyFieldIds.add("")
        }
        playerList = mutableStateOf(startingPlayers)
        startingFieldIds = mutableStateOf(emptyFieldIds)
    }

    var dropdownExpanded = mutableStateOf(false)
    var selectedMap = mutableStateOf(12)
    val playerCount = mutableStateOf(12)
    val mapOptions = mutableStateOf(listOf(12, 6))
    val isFormValid = mutableStateOf(false)

    fun onEvent(event: InitScreenEvent) {
        when (event) {
            is InitScreenEvent.ChangeMapDropdownExpanded -> {
                dropdownExpanded.value = event.expanded
            }
            is InitScreenEvent.ChangeSelectedMap -> {
                selectedMap.value = event.desertSize
                dropdownExpanded.value = false
                resizePlayerList(event.desertSize)
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
        isFormValid.value = playerList.value.all { player ->
            player.id.isNotBlank() &&
                player.water >= 0 &&
                player.spice >= 0
        }
    }

    private fun resizePlayerList(newCount: Int) {
        val currentList = playerList.value.toMutableList()
        val currentFields = startingFieldIds.value.toMutableList()

        if (currentList.size > newCount) {
            playerList.value = currentList.take(newCount)
            startingFieldIds.value = currentFields.take(newCount)
        } else {
            repeat(newCount - currentList.size) {
                playerList.value = playerList.value + startingPlayer.copy()
                startingFieldIds.value = startingFieldIds.value + ""
            }
        }
        playerCount.value = newCount
        validateForm()
    }

    fun savePlayers() {
        // TODO: implementálni a mentést
    }

    sealed class InitScreenEvent {
        data object InitPlayerOnMap : InitScreenEvent()
        data class ChangeMapDropdownExpanded(val expanded: Boolean) : InitScreenEvent()
        data class ChangeSelectedMap(val desertSize: Int) : InitScreenEvent()
        data class UpdatePlayerData(val playerData: Player, val index: Int) : InitScreenEvent()
        data class UpdatePlayerStartingField(val fieldId: String, val index: Int) : InitScreenEvent()
    }
}
