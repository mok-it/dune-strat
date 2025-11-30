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
