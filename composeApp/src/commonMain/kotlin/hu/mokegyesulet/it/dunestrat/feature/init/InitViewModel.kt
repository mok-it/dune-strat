package hu.mokegyesulet.it.dunestrat.feature.init

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import hu.mokegyesulet.it.dunestrat.model.Player

class InitViewModel() : ViewModel() {
    var dropdownExpanded = mutableStateOf(false)
    var selectedMap = mutableStateOf(12)
    val playerCount = mutableStateOf(12)
    val playerList = mutableStateOf(listOf<Player>())
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
            is InitScreenEvent.InitPlayerOnMap -> {}
        }
    }

    sealed class InitScreenEvent() {
        data object InitPlayerOnMap : InitScreenEvent()
        data class ChangeMapDropdownExpanded(val expanded: Boolean) : InitScreenEvent()
        data class ChangeSelectedMap(val desertSize: Int) : InitScreenEvent()
    }
}
