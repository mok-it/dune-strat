package hu.mokegyesulet.it.dunestrat.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.mokegyesulet.it.dunestrat.model.Player
import hu.mokegyesulet.it.dunestrat.model.Student
import hu.mokegyesulet.it.dunestrat.model.Team
import hu.mokegyesulet.it.dunestrat.model.Weapon

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun GlobalStartingConditionsCard(
    modifier: Modifier = Modifier,
    player: Player,
    onChange: (Player) -> Unit,
) {
    val weaponLabels = mapOf(
        Weapon.PISTOL to "Pisztoly",
        Weapon.LASGUN to "Lasgun",
        Weapon.CRYSKNIFE to "Crysknife",
        Weapon.LEGION to "Légió",
    )

    Card(
        modifier = modifier.wrapContentSize()
            .padding(20.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Kezdő állapot", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            NumericTextField(
                value = player.water,
                onValueChange = { onChange(player.copy(water = it)) },
                label = "Víz készlet",
            )

            NumericTextField(
                value = player.spice,
                onValueChange = { onChange(player.copy(spice = it)) },
                label = "Fűszer készlet",
                defaultValue = 10,
            )

            Weapon.entries.forEach { weapon ->
                NumericTextField(
                    value = player.getWeaponCount(weapon),
                    onValueChange = { newValue ->
                        val newWeapons = Weapon.entries.associateWith {
                            player.getWeaponCount(it)
                        }.toMutableMap()
                        newWeapons[weapon] = newValue
                        onChange(player.copy(weapons = newWeapons))
                    },
                    label = weaponLabels[weapon] ?: weapon.name,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerStartingFieldCard(
    index: Int,
    modifier: Modifier = Modifier,
    startingFieldId: String,
    onStartingFieldChange: (String) -> Unit,
    availableStartingFields: List<String>,
    isFieldDuplicate: Boolean,
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("${index + 1}. játékos kezdő mezője", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
            ) {
                OutlinedTextField(
                    value = startingFieldId,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kezdő mező") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor(
                        ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    ).fillMaxWidth(),
                    isError = isFieldDuplicate || startingFieldId.isBlank(),
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    availableStartingFields.forEach { fieldId ->
                        DropdownMenuItem(
                            text = { Text(fieldId) },
                            onClick = {
                                onStartingFieldChange(fieldId)
                                expanded = false
                            },
                        )
                    }
                }
            }

            if (isFieldDuplicate) {
                Text(
                    text = "Ez a mező már ki van választva!",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, top = 6.dp),
                )
            } else if (startingFieldId.isBlank()) {
                Text(
                    text = "Ez a mező kötelező!",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, top = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun NumericTextField(
    value: Int,
    onValueChange: (Int) -> Unit,
    label: String,
    defaultValue: Int = 0,
) {
    TextField(
        value = value.toString(),
        onValueChange = { input ->
            val newValue = input.toIntOrNull()
            if (newValue != null && newValue >= 0) {
                onValueChange(newValue)
            } else if (input.isEmpty()) {
                onValueChange(defaultValue)
            }
        },
        label = { Text(label) },
        isError = value < 0,
    )
}

@Composable
fun TeamConfigurationCard(
    teamIndex: Int,
    team: Team,
    onAddStudent: () -> Unit,
    onRemoveStudent: (Int) -> Unit,
    onUpdateStudent: (Int, Student) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.padding(20.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "${teamIndex + 1}. csapat tagjai",
                    style = MaterialTheme.typography.titleMedium,
                )
                IconButton(onClick = onAddStudent) {
                    Icon(Icons.Default.Add, contentDescription = "Diák hozzáadása")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            team.students.forEachIndexed { studentIndex, student ->
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = student.name,
                                onValueChange = {
                                    onUpdateStudent(studentIndex, student.copy(name = it))
                                },
                                label = { Text("Név") },
                                isError = student.name.isBlank(),
                                modifier = Modifier.fillMaxWidth(),
                            )
                            if (student.name.isBlank()) {
                                Text(
                                    text = "A név nem lehet üres!", // TODO
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 16.dp, top = 6.dp),
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = student.batkabankId,
                                onValueChange = {
                                    onUpdateStudent(studentIndex, student.copy(batkabankId = it))
                                },
                                label = { Text("BatkaBank ID (opcionális)") },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        if (team.students.size > 1) {
                            IconButton(onClick = { onRemoveStudent(studentIndex) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Diák eltávolítása")
                            }
                        }
                    }
                    if (studentIndex < team.students.size - 1) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }
    }
}
