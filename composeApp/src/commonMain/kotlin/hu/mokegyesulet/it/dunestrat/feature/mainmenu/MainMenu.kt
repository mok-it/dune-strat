package hu.mokegyesulet.it.dunestrat.feature.mainmenu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.ui.typography
import hu.mokegyesulet.it.dunestrat.util.drawmap.openURL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(
    onInputMovesClick: (Int) -> Unit,
    onStat: (Int) -> Unit,
    onNewGameClick: () -> Unit,
    onInventoryClick: (Int) -> Unit,
) {
    val viewModel = viewModel { MainMenuViewModel() }

    val loggedIn by viewModel.isLoggedIn.collectAsState(initial = false)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (loggedIn) "Dűne - Főmenü" else "Dűne - Bejelentkezés") })
        },
    ) { paddingValues ->
        if (loggedIn) {
            val games by viewModel.games.collectAsStateWithLifecycle()
            val selectedGame by viewModel.selectedGame
            val latestGameState by viewModel.latestGameState
            val players by viewModel.players

            Row(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(50.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.width(250.dp).fillMaxHeight()) {
                    ExtendedFloatingActionButton(
                        onClick = onNewGameClick,
                        icon = { Icon(Icons.Filled.Add, contentDescription = "Add new game") },
                        text = { Text(text = "Új játék") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(games) { game: Game ->
                            NavigationDrawerItem(
                                label = { Text(text = game.name) },
                                selected = selectedGame == game,
                                onClick = { viewModel.onEvent(MainMenuViewModel.Event.SelectGame(game)) },
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(
                        onClick = {
                            onInputMovesClick(selectedGame?.id ?: -1)
                            viewModel.onEvent(MainMenuViewModel.Event.UnSelectGame)
                        },
                        enabled = selectedGame != null,
                    ) {
                        Text(text = "Lépések bevitele")
                    }
                    Button(
                        onClick = {
                            viewModel.svgDownloadUrl?.let { openURL(it) }
                        },
                        enabled = selectedGame != null && viewModel.svgDownloadUrl != null,
                    ) {
                        Text(text = "Térkép letöltése")
                    }
                    Button(
                        onClick = { selectedGame?.let { onInventoryClick(it.id) } },
                        enabled = selectedGame != null,
                    ) {
                        Text(text = "Csapatok nyersanyagai")
                    }
                    if (selectedGame != null) {
                        if (latestGameState == null) {
                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.weight(1f)
                                    .wrapContentHeight(),
                            ) {
                                item {
                                    Text(
                                        text = "${latestGameState?.index ?: -1 }. Kör",
                                        style = typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                    )
                                }
                                items(players.toList()) { player ->
                                    Text(
                                        text = "${player.id}: ${player.water} víz, ${player.spice} fűszer",
                                    )
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Button(
                        onClick = { onStat(selectedGame?.id ?: -1) },
                        enabled = selectedGame != null,
                    ) {
                        Text(text = "Statisztika")
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
            ) {
                Button(
                    onClick = {
                        viewModel.onLogin()
                    },
                    modifier = Modifier.align(Alignment.Center),
                ) {
                    Text(text = "Bejelentkezés")
                }
            }
        }
    }
}
