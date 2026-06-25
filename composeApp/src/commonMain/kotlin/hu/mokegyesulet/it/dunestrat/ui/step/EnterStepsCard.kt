package hu.mokegyesulet.it.dunestrat.ui.step

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import hu.mokegyesulet.it.dunestrat.model.Weapon

@Composable
fun EnterStepsCard(
    stepCardState: StepCardState,
    onStepCardStateChanged: (StepCardEvent) -> Unit,
) {
    Card {
        Column {
            Text("Player: ${stepCardState.playerId}")
            Row {
                Text("Lelép:")
                TextField(
                    value = stepCardState.leaveFields,
                    onValueChange = {
                        onStepCardStateChanged(StepCardEvent.LeaveFieldsChanged(it))
                    },
                )
            }
            Row {
                Text("Rálép:")
                TextField(
                    value = stepCardState.enterFields,
                    onValueChange = {
                        onStepCardStateChanged(StepCardEvent.EnterFieldsChanged(it))
                    },
                )
            }
            Row {
                Text("Fegyver:")
                for (w in Weapon.entries) {
                    TextField(
                        value = stepCardState.purchaseWeapons[w].toString(),
                        onValueChange = {
                            onStepCardStateChanged(
                                StepCardEvent.PurchaseWeaponsChanged(w, it.toInt()),
                            )
                        },
                    )
                }
            }
            Row {
                Text("Harvester:")
                TextField(
                    value = stepCardState.buildHarvesters,
                    onValueChange = {
                        onStepCardStateChanged(StepCardEvent.BuildHarvestersChanged(it))
                    },
                )
            }
        }
    }
}
