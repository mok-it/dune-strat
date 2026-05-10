package hu.mokegyesulet.it.dunestrat.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hu.mokegyesulet.it.dunestrat.model.Player
import hu.mokegyesulet.it.dunestrat.model.Weapon
import org.jetbrains.compose.ui.tooling.preview.Preview

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
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                )
            } else if (startingFieldId.isBlank()) {
                Text(
                    text = "Ez a mező kötelező!",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp),
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
