package hu.mokegyesulet.it.dunestrat.feature.init

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.mokegyesulet.it.dunestrat.model.Player
import hu.mokegyesulet.it.dunestrat.model.Weapon

class InitViewModel() : ViewModel() {

    private fun createDefaultPlayer() = Player(
        id = "",
        water = 0,
        spice = 10,
        weapons = mapOf(
            Weapon.PISTOL to 0,
            Weapon.LASGUN to 0,
            Weapon.CRYSKNIFE to 0,
            Weapon.LEGION to 0
        ),
        ownedFields = setOf()
    )

    val playerList: MutableState<List<Player>>
    val startingFieldIds: MutableState<List<String>>

    init {
        val startingPlayers = mutableListOf<Player>()
        val emptyFieldIds = mutableListOf<String>()
        repeat(12) {
            startingPlayers.add(createDefaultPlayer())
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
        // Minden mezőnek ki kell lennie töltve
        isFormValid.value = playerList.value.zip(startingFieldIds.value).all { (player, fieldId) ->
            player.id.isNotBlank() &&
                fieldId.isNotBlank() &&
                player.water >= 0 &&
                player.spice >= 0 &&
                player.getWeaponCount(Weapon.PISTOL) >= 0 &&
                player.getWeaponCount(Weapon.LASGUN) >= 0 &&
                player.getWeaponCount(Weapon.CRYSKNIFE) >= 0 &&
                player.getWeaponCount(Weapon.LEGION) >= 0
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
                playerList.value = playerList.value + createDefaultPlayer()
                startingFieldIds.value = startingFieldIds.value + ""
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
        data class ChangeSelectedMap(val desertSize: Int) : InitScreenEvent()
        data class UpdatePlayerData(val playerData: Player, val index: Int) : InitScreenEvent()
        data class UpdatePlayerStartingField(val fieldId: String, val index: Int) : InitScreenEvent()
    }
}
