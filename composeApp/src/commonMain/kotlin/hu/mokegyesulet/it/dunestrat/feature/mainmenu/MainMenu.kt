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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.model.Game
import hu.mokegyesulet.it.dunestrat.util.drawmap.openURL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(
    onInputMovesClick: (Int) -> Unit,
    onStat: (Int) -> Unit,
    onNewGameClick: () -> Unit,
) {
    val viewModel = viewModel { MainMenuViewModel() }

    val loggedIn by viewModel.isLoggedIn.collectAsState(initial = false)

    if (loggedIn) {
        val games by viewModel.games.collectAsStateWithLifecycle()
        val selectedGame by viewModel.selectedGame
        val latestGameState by viewModel.latestGameState
        val players by viewModel.players

        Row(
            modifier = Modifier.fillMaxSize().padding(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            LazyColumn {
                item {
                    Button(onClick = onNewGameClick) {
                        Row {
                            Icon(Icons.Filled.Add, contentDescription = "Add new game")
                            Text(text = "Új játék")
                        }
                    }
                }

                items(games) { game: Game ->
                    Card(
                        border = if (selectedGame == game) {
                            BorderStroke(2.dp, Color.Red)
                        } else {
                            null
                        },
                        modifier = Modifier.clickable {
                            viewModel.onEvent(MainMenuViewModel.Event.SelectGame(game))
                        }.padding(4.dp),
                    ) {
                        Text(
                            text = if (selectedGame ==
                                game
                            ) {
                                "${game.name}(${latestGameState?.index ?: -1 }. kör)"
                            } else {
                                game.name
                            },
                            modifier = Modifier.padding(8.dp),
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
                if (selectedGame != null) {
                    Button(
                        onClick = {
                            openURL(viewModel.svgDownloadUrl!!)
                        },
                    ) {
                        Text(text = "Térkép letöltése")
                    }
                }
                LazyColumn(
                    modifier = Modifier.weight(1f)
                        .wrapContentHeight(),
                ) {
                    items(players.toList()) { player ->
                        Text(text = "${player.id}: ${player.water} víz, ${player.spice} fűszer")
                    }
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
            modifier = Modifier.fillMaxSize(),
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
