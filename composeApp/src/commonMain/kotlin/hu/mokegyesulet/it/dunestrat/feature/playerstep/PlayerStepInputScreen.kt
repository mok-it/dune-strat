package hu.mokegyesulet.it.dunestrat.feature.playerstep

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.model.Weapon
import hu.mokegyesulet.it.dunestrat.ui.tabKeyNavigable

@Composable
fun PlayerStepInputScreen(gameId: Int) {
    val viewModel = viewModel { PlayerStepInputViewModel(gameId) }
    println(viewModel.gameId)
    val tabIndex by viewModel.tabIndex
    val members by viewModel.members

    val game by viewModel.game.collectAsStateWithLifecycle()
    val playerIds = game.teams.map { it.playerId }

    val leaveFields: List<Pair<String, PlayerStepInputViewModel.Validation?>>
        by viewModel.leaveFields

    val enterFields: List<Pair<String, PlayerStepInputViewModel.Validation?>>
        by viewModel.enterFields

    val purchaseWeapons: Map<Weapon, Int> by viewModel.purchaseWeapons

    val uiState by viewModel.uiState
    val purchaseHarvester by uiState.purchaseHarvester

    val gameState by viewModel.gameState.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            PrimaryScrollableTabRow(
                selectedTabIndex = tabIndex,
                modifier = Modifier.fillMaxWidth(),
            ) {
                playerIds.forEachIndexed { index, playerId ->
                    Tab(
                        selected = index == tabIndex,
                        onClick = {
                            viewModel.onEvent(PlayerStepInputViewModel.Event.TabSelected(index))
                        },
                        text = {
                            Text(text = playerId)
                        },
                    )
                }
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
        ) {
            Column {
                Text("Field count: ${gameState.fields.size}")
                Row {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("Lelépés")
                        LazyColumn {
                            itemsIndexed(leaveFields) {
                                    index: Int,
                                    fieldData: Pair<String, PlayerStepInputViewModel.Validation?>,
                                ->
                                FieldRow(
                                    value = fieldData.first,
                                    onValueChange = { it: String ->
                                        viewModel.onEvent(
                                            PlayerStepInputViewModel.Event.LeaveField(
                                                index,
                                                it,
                                            ),
                                        )
                                    },
                                    validation = fieldData.second,
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Vásárlás")
                        Weapon.entries.forEach { weapon ->
                            Row {
                                Text(
                                    text = when (weapon) {
                                        Weapon.PISTOL -> "MPT: "
                                        Weapon.LASGUN -> "LSG: "
                                        Weapon.CRYSKNIFE -> "CRK: "
                                        Weapon.LEGION -> "Légió: "
                                    },
                                )
                                TextField(
                                    value = if ((purchaseWeapons[weapon] ?: 0) != 0) {
                                        (purchaseWeapons[weapon] ?: 0).toString()
                                    } else {
                                        ""
                                    },
                                    placeholder = {
                                        Text(text = "0")
                                    },
                                    onValueChange = {
                                        viewModel.onEvent(
                                            PlayerStepInputViewModel.Event.PurchaseWeapon(
                                                weapon = weapon,
                                                value = it,
                                            ),
                                        )
                                    },
                                    modifier = Modifier.tabKeyNavigable(focusManager),
                                )
                            }
                        }
                        Row {
                            FieldRow(
                                value = purchaseHarvester.first,
                                onValueChange = {
                                    viewModel.onEvent(
                                        PlayerStepInputViewModel.Event.PurchaseHarvester(it),
                                    )
                                },
                                validation = purchaseHarvester.second,
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Rálépés")
                        LazyColumn {
                            itemsIndexed(enterFields) {
                                    index: Int,
                                    fieldData: Pair<String, PlayerStepInputViewModel.Validation?>,
                                ->
                                FieldRow(
                                    value = fieldData.first,
                                    onValueChange = { it: String ->
                                        viewModel.onEvent(
                                            PlayerStepInputViewModel.Event.LeaveField(
                                                index,
                                                it,
                                            ),
                                        )
                                    },
                                    validation = fieldData.second,
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.weight(1f))
                Text("Csapattagok: $members")
            }
        }
    }
}

@Composable
fun FieldRow(
    value: String,
    onValueChange: (String) -> Unit,
    validation: PlayerStepInputViewModel.Validation?,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier.fillMaxWidth().padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f)
                .tabKeyNavigable(focusManager),
        )
        Icon(
            imageVector = when (validation?.isError) {
                null -> Icons.Filled.Check
                true -> Icons.Default.QuestionMark
                false -> Icons.Default.Error
            },
            contentDescription = validation?.message,
            tint = when (validation?.isError) {
                null -> Color.Green
                true -> Color.Red
                false -> Color(1.0f, 0.5f, 0.0f)
            },
        )
    }
}
