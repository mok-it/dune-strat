package hu.mokegyesulet.it.dunestrat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hu.mokegyesulet.it.dunestrat.model.Player
import hu.mokegyesulet.it.dunestrat.model.Weapon
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun CreatePlayerCard(
    index: Int,
    modifier: Modifier,
    player: Player,
    onChange: (Player, Int) -> Unit,
    startingFieldId: String,
    onStartingFieldChange: (String) -> Unit,
) {
    val weaponLabels = mapOf(
        Weapon.PISTOL to "Pisztoly",
        Weapon.LASGUN to "Lasgun",
        Weapon.CRYSKNIFE to "Crysknife",
        Weapon.LEGION to "Légió",
    )

    Card(
        modifier = modifier.padding(20.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            NumericTextField(
                value = player.water,
                onValueChange = { onChange(player.copy(water = it), index) },
                label = "Víz készlet",
            )

            NumericTextField(
                value = player.spice,
                onValueChange = { onChange(player.copy(spice = it), index) },
                label = "Fűszer készlet",
                defaultValue = 10,
            )

            Weapon.entries.forEach { weapon ->
                NumericTextField(
                    value = player.getWeaponCount(weapon),
                    onValueChange = { newValue ->
                        val newWeapons = Weapon.entries.associateWith { player.getWeaponCount(it) }.toMutableMap()
                        newWeapons[weapon] = newValue
                        onChange(player.copy(weapons = newWeapons), index)
                    },
                    label = weaponLabels[weapon] ?: weapon.name,
                )
            }

            TextField(
                value = startingFieldId,
                onValueChange = onStartingFieldChange,
                label = { Text("Kezdő mező") },
                isError = startingFieldId.isBlank(),
            )
            if (startingFieldId.isBlank()) {
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

