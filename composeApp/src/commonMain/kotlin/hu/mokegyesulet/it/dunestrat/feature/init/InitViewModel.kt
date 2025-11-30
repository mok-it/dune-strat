package hu.mokegyesulet.it.dunestrat.feature.init

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dune_strat.composeapp.generated.resources.Res
import hu.mokegyesulet.it.dunestrat.model.Player

class InitViewModel() : ViewModel() {
    private val startingPlayer = Player("", 0,0,mapOf(),setOf(),)
    val playerList: MutableState<List<Player>>
    val startingFieldIds: MutableState<List<String>>
    init {
        val startingPlayers = mutableListOf<Player>()
        val emtpyFieldIds = mutableListOf<String>()
        repeat(12) {
            startingPlayers.add(startingPlayer.copy())
            emtpyFieldIds.add("")
        }
        playerList = mutableStateOf(startingPlayers)
        startingFieldIds = mutableStateOf(emtpyFieldIds)
    }
    var dropdownExpanded = mutableStateOf(false)
    var selectedMap = mutableStateOf(12)
    val playerCount = mutableStateOf(12)
    val mapOptions = mutableStateOf(listOf(12, 6))

    fun onEvent(event: InitScreenEvent) {
        when (event) {
            is InitScreenEvent.ChangeMapDropdownExpanded -> {
                dropdownExpanded.value = event.expanded
            }
            is InitScreenEvent.ChangeSelectedMap -> {
                selectedMap.value = event.desertSize
                dropdownExpanded.value = false
            }
            is InitScreenEvent.UpdatePlayerData -> {
                val newPlayerList = playerList.value.toMutableList()
                newPlayerList[event.index] = event.playerData
                playerList.value = newPlayerList
            }
            is InitScreenEvent.UpdatePlayerStartingField -> {
                val newStartingFieldIds = startingFieldIds.value.toMutableList()
                newStartingFieldIds[event.index] = event.fieldId
                startingFieldIds.value = newStartingFieldIds
            }

            is InitScreenEvent.InitPlayerOnMap -> {}
        }
    }

    sealed class InitScreenEvent() {
        data object InitPlayerOnMap : InitScreenEvent()
        data class ChangeMapDropdownExpanded(val expanded: Boolean) : InitScreenEvent()
        data class ChangeSelectedMap(val desertSize: Int) : InitScreenEvent()
        data class UpdatePlayerData(val playerData: Player, val index: Int) : InitScreenEvent()
        data class UpdatePlayerStartingField(val fieldId : String, val index: Int) : InitScreenEvent()
    }
}
