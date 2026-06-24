package hu.mokegyesulet.it.dunestrat.feature.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.model.Player
import hu.mokegyesulet.it.dunestrat.model.Weapon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    gameId: Int,
    onBack: () -> Unit,
) {
    val viewModel: InventoryViewModel = viewModel { InventoryViewModel(gameId) }
    val players by viewModel.players

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Nyersanyagok - Játék: $gameId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                .padding(16.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart,
            ) {
                Column(modifier = Modifier.widthIn(max = 700.dp)) {
                    Row(
                        modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(text = "Sorszám", modifier = Modifier.width(80.dp))
                        Text(text = "Víz", modifier = Modifier.width(64.dp))
                        Text(text = "Fűszer", modifier = Modifier.width(64.dp))
                        Text(text = "LSG", modifier = Modifier.width(64.dp))
                        Text(text = "MPT", modifier = Modifier.width(64.dp))
                        Text(text = "CRK", modifier = Modifier.width(64.dp))
                        Text(text = "Légió", modifier = Modifier.width(72.dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn(modifier = Modifier.fillMaxHeight()) {
                        items(players) { player ->
                            val alternateBackground = players.indexOf(player) % 2 == 0
                            InventoryRow(
                                player = player,
                                alternateBackground = alternateBackground,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InventoryRow(
    player: Player,
    alternateBackground: Boolean,
) {
    val lsg = player.getWeaponCount(Weapon.LASGUN).toString()
    val mpt = player.getWeaponCount(Weapon.PISTOL).toString()
    val crk = player.getWeaponCount(Weapon.CRYSKNIFE).toString()
    val legion = player.getWeaponCount(Weapon.LEGION).toString()

    val rowContent: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = player.id.toString(), modifier = Modifier.width(80.dp))
            Text(text = player.water.toString(), modifier = Modifier.width(64.dp))
            Text(text = player.spice.toString(), modifier = Modifier.width(64.dp))
            Text(text = lsg, modifier = Modifier.width(64.dp))
            Text(text = mpt, modifier = Modifier.width(64.dp))
            Text(text = crk, modifier = Modifier.width(64.dp))
            Text(text = legion, modifier = Modifier.width(72.dp))
        }
    }

    if (alternateBackground) {
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ) {
            rowContent()
        }
    } else {
        rowContent()
    }
}
