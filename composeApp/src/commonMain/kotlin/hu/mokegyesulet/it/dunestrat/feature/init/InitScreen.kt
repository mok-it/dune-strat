package hu.mokegyesulet.it.dunestrat.feature.init

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.model.Player
import hu.mokegyesulet.it.dunestrat.ui.CreatePlayerCard

// 1. Szám adatok ellenőrzése backenden
// 2. Menü helyes működése
// 3. Mentés gomb
// 4. Desert lista -> legördülő menü
// 5. Új gameState létrehozása

@Composable
fun InitScreen() {
    val viewModel = viewModel { InitViewModel() }
    var expanded by viewModel.dropdownExpanded
    var selectedMap by viewModel.selectedMap
    val playerCount by viewModel.playerCount
    val mapOptions by viewModel.mapOptions
    val playerList by viewModel.playerList
    val startingFieldIds by viewModel.startingFieldIds

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            OutlinedTextField(
                value = "Hexagon – $playerCount játékos",
                onValueChange = {},
                readOnly = true,
                label = { Text("Térkép kiválasztása") },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .clickable { expanded = true },
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                mapOptions.forEach { map ->
                    DropdownMenuItem(
                        text = { "Hexagon – $playerCount játékos" },
                        onClick = {
                            selectedMap = map
                            expanded = false
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            for (i in 0..<  playerCount) {
                item { Text(i.toString() + ". játékos") }
                item {
                    CreatePlayerCard(
                        index = i,
                        modifier = Modifier.fillMaxWidth(0.45f),
                        player = playerList[i],
                        onChange = {player, index ->
                            viewModel.onEvent(InitViewModel.InitScreenEvent.UpdatePlayerData(player, index))
                        },
                        onStartingFieldChange = {fieldId ->
                            viewModel.onEvent(InitViewModel.InitScreenEvent.UpdatePlayerStartingField(fieldId = fieldId, i))
                        },
                        startingFieldId = startingFieldIds[i]
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            item {
                Button(
                    onClick = { viewModel.savePlayers() },
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text("Adatok mentése")
                }
            }
        }
    }
}

private fun InitViewModel.savePlayers() {
    TODO("savePlayers függvény hiányos")
}
