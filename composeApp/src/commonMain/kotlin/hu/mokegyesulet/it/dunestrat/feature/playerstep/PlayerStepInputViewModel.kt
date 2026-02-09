package hu.mokegyesulet.it.dunestrat.feature.playerstep

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.model.GameProgress
import hu.mokegyesulet.it.dunestrat.model.Team

class PlayerStepInputViewModel : ViewModel() {

    val tabIndex = mutableStateOf(0)

    val game = mutableStateOf(
        Game(
            name = "Teszt",
            progress = GameProgress.ONGOING,
            teams = (1..12).map { Team(playerId = it.toString(), students = listOf()) },
        ),
    )

    fun onEvent(event: Event) {
        when (event) {
            is Event.TabSelected -> tabIndex.value = event.index
        }
    }

    sealed class Event {
        data class TabSelected(val index: Int) : Event()
    }
}
