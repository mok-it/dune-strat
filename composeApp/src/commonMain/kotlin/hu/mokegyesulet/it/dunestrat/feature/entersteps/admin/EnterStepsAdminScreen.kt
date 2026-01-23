package hu.mokegyesulet.it.dunestrat.feature.entersteps.admin

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.model.Player
import hu.mokegyesulet.it.dunestrat.ui.step.EnterStepsCard

@Composable
fun EnterStepsAdminScreen(players: List<Player>) {
    val viewModel = viewModel { EnterStepsAdminViewModel(players) }
    val steps = viewModel.stepStates.value
    LazyColumn(
        modifier = Modifier.fillMaxWidth(1f),
    ) {
        itemsIndexed(steps) { index, step ->
            EnterStepsCard(step, viewModel.onStepCardStateChanged(index))
        }
    }
}
