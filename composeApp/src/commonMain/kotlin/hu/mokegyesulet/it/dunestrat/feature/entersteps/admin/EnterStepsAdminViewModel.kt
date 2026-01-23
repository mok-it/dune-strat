package hu.mokegyesulet.it.dunestrat.feature.entersteps.admin

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hu.mokegyesulet.it.dunestrat.model.Player
import hu.mokegyesulet.it.dunestrat.ui.step.StepCardEvent
import hu.mokegyesulet.it.dunestrat.ui.step.StepCardState

class EnterStepsAdminViewModel(players: List<Player>) : ViewModel() {
    private val _stepStates: MutableList<StepCardState> = ArrayList()
    init {
        for (p in players) {
            _stepStates.add(StepCardState(p.id))
        }
    }
    val stepStates: MutableState<List<StepCardState>>
        get() = mutableStateOf(_stepStates)

    private fun onStepCardStateChanged(
        event: StepCardEvent,
        playerIndex: Int,
    ) {
        when (event) {
            is StepCardEvent.EnterFieldsChanged -> _stepStates[playerIndex] =
                _stepStates[playerIndex].copy(enterFields = event.enterFields)

            is StepCardEvent.LeaveFieldsChanged -> _stepStates[playerIndex] =
                _stepStates[playerIndex].copy(leaveFields = event.leaveFields)

            is StepCardEvent.BuildHarvestersChanged -> _stepStates[playerIndex] =
                _stepStates[playerIndex].copy(buildHarvesters = event.buildHarvesters)

            is StepCardEvent.PurchaseWeaponsChanged -> {
                val x = _stepStates[playerIndex].purchaseWeapons.toMutableMap()
                x[event.weaponType] = event.amount
                _stepStates[playerIndex] = _stepStates[playerIndex].copy(purchaseWeapons = x)
            }
        }
    }

    fun onStepCardStateChanged(playerIndex: Int): (StepCardEvent) -> Unit = { e ->
        onStepCardStateChanged(e, playerIndex)
    }
}
