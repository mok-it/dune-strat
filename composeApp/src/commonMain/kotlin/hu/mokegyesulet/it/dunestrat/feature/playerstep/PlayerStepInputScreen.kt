package hu.mokegyesulet.it.dunestrat.feature.playerstep

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.model.Weapon
import hu.mokegyesulet.it.dunestrat.ui.tabKeyNavigable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerStepInputScreen(
    gameId: Int,
    onBack: () -> Unit,
) {
    val viewModel = viewModel { PlayerStepInputViewModel(gameId) }
    val tabIndex by viewModel.tabIndex
    val members by viewModel.members

    val game by viewModel.game
    val gameState by viewModel.gameState

    if (game == null || gameState == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Loading...")
        }
    } else {
        val playerIds = game!!.teams.map { it.playerId }

        val leaveFields: List<Pair<String, PlayerStepInputViewModel.Validation?>>
            by viewModel.leaveFields

        val enterFields: List<Pair<String, PlayerStepInputViewModel.Validation?>>
            by viewModel.enterFields

        val purchaseWeapons: Map<Weapon, Int> by viewModel.purchaseWeapons

        val uiState by viewModel.uiState
        val purchaseHarvester by uiState.purchaseHarvester

        val saveOnLostFocus: (FocusState) -> Unit = {
            if (!it.isFocused) {
                viewModel.onEvent(
                    PlayerStepInputViewModel.Event.SaveToDatabase,
                )
            }
        }

        val lifecycleOwner = LocalLifecycleOwner.current
        DisposableEffect(lifecycleOwner) {
            onDispose { viewModel.onEvent(PlayerStepInputViewModel.Event.SaveToDatabase) }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {},
                    actions = {
                        Button(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            onClick = {
                                viewModel.onEvent(
                                    PlayerStepInputViewModel.Event.SaveToDatabase,
                                )
                            },
                        ) {
                            Text(text = "Mentés")
                        }

                        Button(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            onClick = {
                                viewModel.onEvent(
                                    PlayerStepInputViewModel.Event.RunTurn,
                                )
                                onBack()
                            },
                        ) {
                            Text(text = "Kör futtatása")
                        }
                    },
                )
            },
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
                                Text(text = playerId.toString())
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
                    Row {
                        FieldInputColumn(
                            title = "Lelépés",
                            fields = leaveFields,
                            onValueChange = { index, value ->
                                viewModel.onEvent(
                                    PlayerStepInputViewModel.Event.LeaveField(index, value),
                                )
                            },
                            onFocusChanged = saveOnLostFocus,
                            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                        ) {
                            Text("Vásárlás")

                            val focusManager = LocalFocusManager.current

                            Weapon.entries.forEach { weapon ->
                                TextField(
                                    label = {
                                        Text(
                                            text = when (weapon) {
                                                Weapon.PISTOL -> "MPT"
                                                Weapon.LASGUN -> "LSG"
                                                Weapon.CRYSKNIFE -> "CRK"
                                                Weapon.LEGION -> "Légió"
                                            },

                                        )
                                    },
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
                                    modifier = Modifier.onFocusChanged(
                                        saveOnLostFocus,
                                    ).tabKeyNavigable(focusManager)
                                        .padding(horizontal = 4.dp, vertical = 6.dp),
                                )
                            }
                            FieldRow(
                                value = purchaseHarvester.first,
                                label = "Harvester",
                                onValueChange = {
                                    viewModel.onEvent(
                                        PlayerStepInputViewModel.Event.PurchaseHarvester(it),
                                    )
                                },
                                validation = purchaseHarvester.second,
                                modifier = Modifier.onFocusChanged(
                                    saveOnLostFocus,
                                ).tabKeyNavigable(focusManager)
                                    .padding(horizontal = 4.dp, vertical = 26.dp),
                            )
                        }
                        FieldInputColumn(
                            title = "Rálépés",
                            fields = enterFields,
                            onValueChange = { index, value ->
                                viewModel.onEvent(
                                    PlayerStepInputViewModel.Event.EnterField(index, value),
                                )
                            },
                            onFocusChanged = saveOnLostFocus,
                            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                        )
                    }

                    Spacer(Modifier.weight(1f))
                    Text("Csapattagok: $members")
                }
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
    label: String? = null,
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .tabKeyNavigable(focusManager),
            label = if (label != null) {
                { Text(text = label) }
            } else {
                null
            },
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

@Composable
private fun FieldInputColumn(
    title: String,
    fields: List<Pair<String, PlayerStepInputViewModel.Validation?>>,
    onValueChange: (Int, String) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = title)
        LazyColumn {
            itemsIndexed(fields) {
                    index: Int,
                    fieldData: Pair<String, PlayerStepInputViewModel.Validation?>,
                ->
                FieldRow(
                    value = fieldData.first,
                    onValueChange = {
                        onValueChange(index, it)
                    },
                    validation = fieldData.second,
                    modifier = Modifier.onFocusChanged(onFocusChanged),
                )
            }
        }
    }
}
