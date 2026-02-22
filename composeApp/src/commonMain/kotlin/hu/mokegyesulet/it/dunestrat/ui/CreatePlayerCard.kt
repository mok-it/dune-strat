package hu.mokegyesulet.it.dunestrat.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
        TextField(
            value = player.id,
            onValueChange = { onChange(player.copy(id = it), index) },
            label = { Text("Játékos ID") }
        )

        TextField(
            value = player.water.toString(),
            onValueChange = { onChange(player.copy(water = it.toInt()), index) },
            label = { Text("Víz készlet") }
        )

        TextField(
            value = player.spice.toString(),
            onValueChange = { onChange(player.copy(spice = it.toInt()), index) },
            label = { Text("Fűszer készlet") }
        )

        TextField(
            value = player.getWeaponCount(Weapon.PISTOL).toString(),
            onValueChange = {
                val newWeapons: Map<Weapon, Int> = mapOf(
                    Weapon.PISTOL to it.toInt(),
                    Weapon.LASGUN to player.getWeaponCount( Weapon.LASGUN),
                    Weapon.CRYSKNIFE to player.getWeaponCount( Weapon.CRYSKNIFE),
                    Weapon.LEGION to player.getWeaponCount( Weapon.LEGION)
                )
                val newPlayer = player.copy(weapons = newWeapons)
                onChange(newPlayer, index)
            },
            label = { Text("Pisztoly") }
        )

        TextField(
            value = player.getWeaponCount(Weapon.LASGUN).toString(),
            onValueChange = {
                val newWeapons: Map<Weapon, Int> = mapOf(
                    Weapon.PISTOL to player.getWeaponCount( Weapon.PISTOL),
                    Weapon.LASGUN to it.toInt(),
                    Weapon.CRYSKNIFE to player.getWeaponCount( Weapon.CRYSKNIFE),
                    Weapon.LEGION to player.getWeaponCount( Weapon.LEGION)
                )
                val newPlayer = player.copy(weapons = newWeapons)
                onChange(newPlayer, index)
            },
            label = { Text("Lasgun") }
        )

        TextField(
            value = player.getWeaponCount(Weapon.CRYSKNIFE).toString(),
            onValueChange = {
                val newWeapons: Map<Weapon, Int> = mapOf(
                    Weapon.PISTOL to player.getWeaponCount( Weapon.PISTOL),
                    Weapon.LASGUN to player.getWeaponCount( Weapon.LASGUN),
                    Weapon.CRYSKNIFE to it.toInt(),
                    Weapon.LEGION to player.getWeaponCount( Weapon.LEGION)
                )
                val newPlayer = player.copy(weapons = newWeapons)
                onChange(newPlayer, index)
            },
            label = { Text("Crysknife") }
        )

        TextField(
            value = player.getWeaponCount(Weapon.LEGION).toString(),
            onValueChange = {
                val newWeapons: Map<Weapon, Int> = mapOf(
                    Weapon.PISTOL to player.getWeaponCount( Weapon.PISTOL),
                    Weapon.LASGUN to player.getWeaponCount( Weapon.LASGUN),
                    Weapon.CRYSKNIFE to player.getWeaponCount( Weapon.CRYSKNIFE),
                    Weapon.LEGION to it.toInt()
                )
                val newPlayer = player.copy(weapons = newWeapons)
                onChange(newPlayer, index)
            },
            label = { Text("Légió") }
        )

        TextField(
            value = startingFieldId,
            onValueChange = onStartingFieldChange,
            label = { Text("Kezdő mező") }
        )
    }
}

/*
package hu.mokegyesulet.it.dunestrat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    // Hibaüzenetek state-jei
    val waterError = remember { mutableStateOf<String?>(null) }
    val spiceError = remember { mutableStateOf<String?>(null) }
    val pistolError = remember { mutableStateOf<String?>(null) }
    val lasgunError = remember { mutableStateOf<String?>(null) }
    val crysknifeError = remember { mutableStateOf<String?>(null) }
    val legionError = remember { mutableStateOf<String?>(null) }

    Card(
        modifier = modifier.padding(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = player.id,
                onValueChange = { onChange(player.copy(id = it), index) },
                label = { Text("Játékos ID") }
            )

            // Víz készlet - try-catch
            Column {
                TextField(
                    value = player.water.toString(),
                    onValueChange = { input ->
                        try {
                            val value = input.toIntOrNull()
                            if (value != null) {
                                onChange(player.copy(water = value), index)
                                waterError.value = null
                            } else if (input.isNotEmpty()) {
                                waterError.value = "Kérlek, csak számot adj meg!"
                            } else {
                                waterError.value = null
                            }
                        } catch (e: NumberFormatException) {
                            waterError.value = "Érvénytelen szám!"
                        }
                    },
                    label = { Text("Víz készlet") },
                    isError = waterError.value != null
                )
                waterError.value?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            // Fűszer készlet - try-catch
            Column {
                TextField(
                    value = player.spice.toString(),
                    onValueChange = { input ->
                        try {
                            val value = input.toIntOrNull()
                            if (value != null) {
                                onChange(player.copy(spice = value), index)
                                spiceError.value = null
                            } else if (input.isNotEmpty()) {
                                spiceError.value = "Kérlek, csak számot adj meg!"
                            } else {
                                spiceError.value = null
                            }
                        } catch (e: NumberFormatException) {
                            spiceError.value = "Érvénytelen szám!"
                        }
                    },
                    label = { Text("Fűszer készlet") },
                    isError = spiceError.value != null
                )
                spiceError.value?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            // Pisztoly - try-catch
            Column {
                TextField(
                    value = player.getWeaponCount(Weapon.PISTOL).toString(),
                    onValueChange = { input ->
                        try {
                            val value = input.toIntOrNull()
                            if (value != null) {
                                val newWeapons: Map<Weapon, Int> = mapOf(
                                    Weapon.PISTOL to value,
                                    Weapon.LASGUN to player.getWeaponCount(Weapon.LASGUN),
                                    Weapon.CRYSKNIFE to player.getWeaponCount(Weapon.CRYSKNIFE),
                                    Weapon.LEGION to player.getWeaponCount(Weapon.LEGION)
                                )
                                val newPlayer = player.copy(weapons = newWeapons)
                                onChange(newPlayer, index)
                                pistolError.value = null
                            } else if (input.isNotEmpty()) {
                                pistolError.value = "Kérlek, csak számot adj meg!"
                            } else {
                                pistolError.value = null
                            }
                        } catch (e: NumberFormatException) {
                            pistolError.value = "Érvénytelen szám!"
                        }
                    },
                    label = { Text("Pisztoly") },
                    isError = pistolError.value != null
                )
                pistolError.value?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            // Lasgun - try-catch
            Column {
                TextField(
                    value = player.getWeaponCount(Weapon.LASGUN).toString(),
                    onValueChange = { input ->
                        try {
                            val value = input.toIntOrNull()
                            if (value != null) {
                                val newWeapons: Map<Weapon, Int> = mapOf(
                                    Weapon.PISTOL to player.getWeaponCount(Weapon.PISTOL),
                                    Weapon.LASGUN to value,
                                    Weapon.CRYSKNIFE to player.getWeaponCount(Weapon.CRYSKNIFE),
                                    Weapon.LEGION to player.getWeaponCount(Weapon.LEGION)
                                )
                                val newPlayer = player.copy(weapons = newWeapons)
                                onChange(newPlayer, index)
                                lasgunError.value = null
                            } else if (input.isNotEmpty()) {
                                lasgunError.value = "Kérlek, csak számot adj meg!"
                            } else {
                                lasgunError.value = null
                            }
                        } catch (e: NumberFormatException) {
                            lasgunError.value = "Érvénytelen szám!"
                        }
                    },
                    label = { Text("Lasgun") },
                    isError = lasgunError.value != null
                )
                lasgunError.value?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            // Crysknife - try-catch
            Column {
                TextField(
                    value = player.getWeaponCount(Weapon.CRYSKNIFE).toString(),
                    onValueChange = { input ->
                        try {
                            val value = input.toIntOrNull()
                            if (value != null) {
                                val newWeapons: Map<Weapon, Int> = mapOf(
                                    Weapon.PISTOL to player.getWeaponCount(Weapon.PISTOL),
                                    Weapon.LASGUN to player.getWeaponCount(Weapon.LASGUN),
                                    Weapon.CRYSKNIFE to value,
                                    Weapon.LEGION to player.getWeaponCount(Weapon.LEGION)
                                )
                                val newPlayer = player.copy(weapons = newWeapons)
                                onChange(newPlayer, index)
                                crysknifeError.value = null
                            } else if (input.isNotEmpty()) {
                                crysknifeError.value = "Kérlek, csak számot adj meg!"
                            } else {
                                crysknifeError.value = null
                            }
                        } catch (e: NumberFormatException) {
                            crysknifeError.value = "Érvénytelen szám!"
                        }
                    },
                    label = { Text("Crysknife") },
                    isError = crysknifeError.value != null
                )
                crysknifeError.value?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            // Légió - try-catch
            Column {
                TextField(
                    value = player.getWeaponCount(Weapon.LEGION).toString(),
                    onValueChange = { input ->
                        try {
                            val value = input.toIntOrNull()
                            if (value != null) {
                                val newWeapons: Map<Weapon, Int> = mapOf(
                                    Weapon.PISTOL to player.getWeaponCount(Weapon.PISTOL),
                                    Weapon.LASGUN to player.getWeaponCount(Weapon.LASGUN),
                                    Weapon.CRYSKNIFE to player.getWeaponCount(Weapon.CRYSKNIFE),
                                    Weapon.LEGION to value
                                )
                                val newPlayer = player.copy(weapons = newWeapons)
                                onChange(newPlayer, index)
                                legionError.value = null
                            } else if (input.isNotEmpty()) {
                                legionError.value = "Kérlek, csak számot adj meg!"
                            } else {
                                legionError.value = null
                            }
                        } catch (e: NumberFormatException) {
                            legionError.value = "Érvénytelen szám!"
                        }
                    },

        TextField(
            value = startingFieldId,
            onValueChange = onStartingFieldChange,
            label = { Text("Kezdő mező") }
        )
    }
}


 */
