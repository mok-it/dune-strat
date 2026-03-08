package hu.mokegyesulet.it.dunestrat.feature.init

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.ui.CreatePlayerCard

@Composable
fun InitScreen() {
    val viewModel = viewModel { InitViewModel() }
    var expanded by viewModel.dropdownExpanded
    var selectedMap by viewModel.selectedMap
    val playerCount by viewModel.playerCount
    val mapOptions by viewModel.mapOptions
    val playerList by viewModel.playerList
    val startingFieldIds by viewModel.startingFieldIds
    val isFormValid by viewModel.isFormValid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Dropdown
        Box(
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            OutlinedTextField(
                value = "Hexagon – $playerCount játékos",
                onValueChange = {},
                readOnly = true,
                label = { Text("Térkép kiválasztása") },
                modifier = Modifier.fillMaxWidth()
            )

            // Átlátszó klikkelő réteg a TextField felett
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        expanded = !expanded
                    }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                mapOptions.forEach { map ->
                    DropdownMenuItem(
                        text = { Text("Hexagon – $map játékos") },
                        onClick = {
                            viewModel.onEvent(InitViewModel.InitScreenEvent.ChangeSelectedMap(map))
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            for (playerNumber in 1..playerCount) {
                val index = playerNumber - 1

                item { Text("$playerNumber. játékos") }

                item {
                    CreatePlayerCard(
                        index = index,
                        modifier = Modifier.fillMaxWidth(0.45f),
                        player = playerList[index],
                        onChange = { player, i ->
                            viewModel.onEvent(
                                InitViewModel.InitScreenEvent.UpdatePlayerData(player, i)
                            )
                        },
                        onStartingFieldChange = { fieldId ->
                            viewModel.onEvent(
                                InitViewModel.InitScreenEvent.UpdatePlayerStartingField(fieldId, index)
                            )
                        },
                        startingFieldId = startingFieldIds[index]
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Mentés gomb
            item {
                Button(
                    onClick = { viewModel.savePlayers() },
                    enabled = isFormValid,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Adatok mentése")
                }
            }
        }
    }
}
