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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.model.Desert
import hu.mokegyesulet.it.dunestrat.ui.CreatePlayerCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitScreen() {
    val viewModel = viewModel { InitViewModel() }
    val expanded by viewModel.dropdownExpanded
//    val selectedMap by viewModel.selectedMap
    val playerCount by viewModel.playerCount
    val mapOptions: List<Desert> by viewModel.mapOptions.collectAsStateWithLifecycle()
    val playerList by viewModel.playerList
    val startingFieldIds by viewModel.startingFieldIds
    val isFormValid by viewModel.isFormValid
    val selectedDesert by viewModel.selectedDesert

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Dropdown
        Box(
            modifier = Modifier.fillMaxWidth(0.7f),
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    viewModel.onEvent(
                        InitViewModel.InitScreenEvent.ChangeMapDropdownExpanded(!expanded),
                    )
                },

            ) {
                OutlinedTextField(
                    modifier = Modifier.menuAnchor(
                        ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        true,
                    ).fillMaxWidth(),
                    value = selectedDesert?.name?.ifBlank {
                        "${selectedDesert?.fields?.count { it.startingField }} játékos térkép"
                    } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Térkép kiválasztása") },
                    trailingIcon = { TrailingIcon(expanded = expanded) },
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        viewModel.onEvent(
                            InitViewModel.InitScreenEvent.ChangeMapDropdownExpanded(false),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.7f),
                ) {
                    mapOptions.forEach { map ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = map.name.ifBlank {
                                        "${map.fields.count { it.startingField }} játékos térkép"
                                    },
                                )
                            },
                            onClick = {
                                viewModel.onEvent(
                                    InitViewModel.InitScreenEvent.ChangeSelectedMap(map),
                                )
                            },
                        )
                    }
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
                                InitViewModel.InitScreenEvent.UpdatePlayerData(player, i),
                            )
                        },
                        onStartingFieldChange = { fieldId ->
                            viewModel.onEvent(
                                InitViewModel.InitScreenEvent.UpdatePlayerStartingField(
                                    fieldId,
                                    index,
                                ),
                            )
                        },
                        startingFieldId = startingFieldIds[index],
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Mentés gomb
            item {
                Button(
                    onClick = { viewModel.savePlayers() },
                    enabled = isFormValid,
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text("Adatok mentése")
                }
            }
        }
    }
}
