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
    index : Int,
    modifier: Modifier,
    player: Player,
    onChange: (Player, Int) -> Unit,
    startingFieldId: String,
    onStartingFieldChange: (String) -> Unit
) {
    Card(
        modifier = modifier.padding(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = player.id,
                onValueChange = { onChange(player.copy(id = it), index) },
                label = { Text("Játékos ID") },
                isError = player.id.isBlank()
            )
            if (player.id.isBlank()) {
                Text(
                    text = "Ez a mező kötelező!",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }

            TextField(
                value = player.water.toString(),
                onValueChange = { input ->
                    val value = input.toIntOrNull()
                    if (value != null && value >= 0) {
                        onChange(player.copy(water = value), index)
                    } else if (input.isEmpty()) {
                        onChange(player.copy(water = 0), index)
                    }
                },
                label = { Text("Víz készlet") },
                isError = player.water < 0
            )

            TextField(
                value = player.spice.toString(),
                onValueChange = { input ->
                    val value = input.toIntOrNull()
                    if (value != null && value >= 0) {
                        onChange(player.copy(spice = value), index)
                    } else if (input.isEmpty()) {
                        onChange(player.copy(spice = 10), index)
                    }
                },
                label = { Text("Fűszer készlet") },
                isError = player.spice < 0
            )

            TextField(
                value = player.getWeaponCount(Weapon.PISTOL).toString(),
                onValueChange = { input ->
                    val value = input.toIntOrNull()
                    if (value != null && value >= 0) {
                        val currentWeapons = mutableMapOf(
                            Weapon.PISTOL to value,
                            Weapon.LASGUN to player.getWeaponCount(Weapon.LASGUN),
                            Weapon.CRYSKNIFE to player.getWeaponCount(Weapon.CRYSKNIFE),
                            Weapon.LEGION to player.getWeaponCount(Weapon.LEGION)
                        )
                        onChange(player.copy(weapons = currentWeapons), index)
                    } else if (input.isEmpty()) {
                        val currentWeapons = mutableMapOf(
                            Weapon.PISTOL to 0,
                            Weapon.LASGUN to player.getWeaponCount(Weapon.LASGUN),
                            Weapon.CRYSKNIFE to player.getWeaponCount(Weapon.CRYSKNIFE),
                            Weapon.LEGION to player.getWeaponCount(Weapon.LEGION)
                        )
                        onChange(player.copy(weapons = currentWeapons), index)
                    }
                },
                label = { Text("Pisztoly") }
            )

            TextField(
                value = player.getWeaponCount(Weapon.LASGUN).toString(),
                onValueChange = { input ->
                    val value = input.toIntOrNull()
                    if (value != null && value >= 0) {
                        val currentWeapons = mutableMapOf(
                            Weapon.PISTOL to player.getWeaponCount(Weapon.PISTOL),
                            Weapon.LASGUN to value,
                            Weapon.CRYSKNIFE to player.getWeaponCount(Weapon.CRYSKNIFE),
                            Weapon.LEGION to player.getWeaponCount(Weapon.LEGION)
                        )
                        onChange(player.copy(weapons = currentWeapons), index)
                    } else if (input.isEmpty()) {
                        val currentWeapons = mutableMapOf(
                            Weapon.PISTOL to player.getWeaponCount(Weapon.PISTOL),
                            Weapon.LASGUN to 0,
                            Weapon.CRYSKNIFE to player.getWeaponCount(Weapon.CRYSKNIFE),
                            Weapon.LEGION to player.getWeaponCount(Weapon.LEGION)
                        )
                        onChange(player.copy(weapons = currentWeapons), index)
                    }
                },
                label = { Text("Lasgun") }
            )

            TextField(
                value = player.getWeaponCount(Weapon.CRYSKNIFE).toString(),
                onValueChange = { input ->
                    val value = input.toIntOrNull()
                    if (value != null && value >= 0) {
                        val currentWeapons = mutableMapOf(
                            Weapon.PISTOL to player.getWeaponCount(Weapon.PISTOL),
                            Weapon.LASGUN to player.getWeaponCount(Weapon.LASGUN),
                            Weapon.CRYSKNIFE to value,
                            Weapon.LEGION to player.getWeaponCount(Weapon.LEGION)
                        )
                        onChange(player.copy(weapons = currentWeapons), index)
                    } else if (input.isEmpty()) {
                        val currentWeapons = mutableMapOf(
                            Weapon.PISTOL to player.getWeaponCount(Weapon.PISTOL),
                            Weapon.LASGUN to player.getWeaponCount(Weapon.LASGUN),
                            Weapon.CRYSKNIFE to 0,
                            Weapon.LEGION to player.getWeaponCount(Weapon.LEGION)
                        )
                        onChange(player.copy(weapons = currentWeapons), index)
                    }
                },
                label = { Text("Crysknife") }
            )

            TextField(
                value = player.getWeaponCount(Weapon.LEGION).toString(),
                onValueChange = { input ->
                    val value = input.toIntOrNull()
                    if (value != null && value >= 0) {
                        val currentWeapons = mutableMapOf(
                            Weapon.PISTOL to player.getWeaponCount(Weapon.PISTOL),
                            Weapon.LASGUN to player.getWeaponCount(Weapon.LASGUN),
                            Weapon.CRYSKNIFE to player.getWeaponCount(Weapon.CRYSKNIFE),
                            Weapon.LEGION to value
                        )
                        onChange(player.copy(weapons = currentWeapons), index)
                    } else if (input.isEmpty()) {
                        val currentWeapons = mutableMapOf(
                            Weapon.PISTOL to player.getWeaponCount(Weapon.PISTOL),
                            Weapon.LASGUN to player.getWeaponCount(Weapon.LASGUN),
                            Weapon.CRYSKNIFE to player.getWeaponCount(Weapon.CRYSKNIFE),
                            Weapon.LEGION to 0
                        )
                        onChange(player.copy(weapons = currentWeapons), index)
                    }
                },
                label = { Text("Légió") }
            )

            TextField(
                value = startingFieldId,
                onValueChange = onStartingFieldChange,
                label = { Text("Kezdő mező") },
                isError = startingFieldId.isBlank()
            )
            if (startingFieldId.isBlank()) {
                Text(
                    text = "Ez a mező kötelező!",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    }
}
