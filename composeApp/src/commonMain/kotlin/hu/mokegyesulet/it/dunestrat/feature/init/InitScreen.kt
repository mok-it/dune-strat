package hu.mokegyesulet.it.dunestrat.feature.init

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.model.Desert
import hu.mokegyesulet.it.dunestrat.ui.GlobalStartingConditionsCard
import hu.mokegyesulet.it.dunestrat.ui.PlayerStartingFieldCard
import hu.mokegyesulet.it.dunestrat.ui.TeamConfigurationCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitScreen(onNavigateBack: () -> Unit) {
    val viewModel: InitViewModel = viewModel { InitViewModel() }
    val phase by viewModel.currentPhase
    val expanded by viewModel.dropdownExpanded
    val playerCount by viewModel.playerCount
    val mapOptions: List<Desert> by viewModel.mapOptions.collectAsStateWithLifecycle()
    val startingFieldIds by viewModel.startingFieldIds
    val teams by viewModel.teams
    val isFormValid by viewModel.isFormValid
    val selectedDesert by viewModel.selectedDesert
    val basePlayer by viewModel.basePlayerState
    val gameName by viewModel.gameName

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Új játék indítása") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Vissza")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (phase == InitializationPhase.STARTING_CONDITIONS) {
                // Dropdown
                Column(
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
                                "${selectedDesert?.fields?.count {
                                    it.startingField
                                }} játékos térkép"
                            } ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Térkép kiválasztása") },
                            trailingIcon = { TrailingIcon(expanded = expanded) },
                            isError = selectedDesert == null,
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
                                                "${map.fields.count {
                                                    it.startingField
                                                }} játékos térkép"
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
                    if (selectedDesert == null) {
                        Text(
                            text = "A térkép kiválasztása kötelező!",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 16.dp, top = 6.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (phase == InitializationPhase.STARTING_CONDITIONS) {
                    if (selectedDesert != null) {
                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                TextField(
                                    value = gameName,
                                    onValueChange = { input ->
                                        viewModel.onEvent(
                                            InitViewModel.InitScreenEvent.ChangeGameName(input),
                                        )
                                    },
                                    label = { Text("Játék neve") },
                                    isError = gameName.trim() == "",
                                )
                                if (gameName.trim() == "") {
                                    Text(
                                        text = "A játék neve nem lehet üres!",
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(start = 16.dp, top = 6.dp),
                                    )
                                }
                            }
                        }

                        item {
                            GlobalStartingConditionsCard(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                player = basePlayer,
                                onChange = {
                                    viewModel.onEvent(
                                        InitViewModel.InitScreenEvent.UpdateGlobalStartingConditions(
                                            it,
                                        ),
                                    )
                                },
                            )
                        }

                        items(playerCount) { index ->
                            val currentFieldId = startingFieldIds[index]
                            val isDuplicate =
                                startingFieldIds.count { it == currentFieldId && it.isNotBlank() } >
                                    1
                            val availableFields =
                                selectedDesert?.fields?.filter { it.startingField }?.map { it.id }
                                    ?: emptyList()

                            PlayerStartingFieldCard(
                                index = index,
                                modifier = Modifier.fillMaxWidth(0.45f),
                                startingFieldId = currentFieldId,
                                onStartingFieldChange = { fieldId ->
                                    viewModel.onEvent(
                                        InitViewModel.InitScreenEvent.UpdatePlayerStartingField(
                                            fieldId,
                                            index,
                                        ),
                                    )
                                },
                                availableStartingFields = availableFields,
                                isFieldDuplicate = isDuplicate,
                            )
                        }

                        item {
                            Button(
                                onClick = {
                                    viewModel.onEvent(InitViewModel.InitScreenEvent.ProceedToTeams)
                                },
                                enabled = isFormValid,
                                modifier = Modifier.padding(16.dp),
                            ) {
                                Text("Tovább a csapatokhoz")
                            }
                        }
                    }
                } else {
                    item {
                        Text(
                            text = "Csapatok beállítása",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(16.dp),
                        )
                    }

                    items(teams.size) { teamIndex ->
                        TeamConfigurationCard(
                            teamIndex = teamIndex,
                            team = teams[teamIndex],
                            onAddStudent = {
                                viewModel.onEvent(
                                    InitViewModel.InitScreenEvent.AddStudent(teamIndex),
                                )
                            },
                            onRemoveStudent = { studentIndex ->
                                viewModel.onEvent(
                                    InitViewModel.InitScreenEvent.RemoveStudent(
                                        teamIndex,
                                        studentIndex,
                                    ),
                                )
                            },
                            onUpdateStudent = { studentIndex, student ->
                                viewModel.onEvent(
                                    InitViewModel.InitScreenEvent.UpdateStudent(
                                        teamIndex,
                                        studentIndex,
                                        student,
                                    ),
                                )
                            },
                            modifier = Modifier.fillMaxWidth(0.9f),
                        )
                    }

                    item {
                        Row(modifier = Modifier.padding(16.dp)) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.onEvent(
                                        InitViewModel.InitScreenEvent.BackToConditions,
                                    )
                                },
                                modifier = Modifier.padding(end = 8.dp),
                            ) {
                                Text("Vissza")
                            }
                            Button(
                                onClick = { viewModel.savePlayers() },
                                enabled = isFormValid,
                            ) {
                                Text("Játék indítása")
                            }
                        }
                    }
                }
            }
        }
    }
}
