package hu.mokegyesulet.it.dunestrat.feature.mainmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dune_strat.composeapp.generated.resources.Res
import dune_strat.composeapp.generated.resources.grid
import hu.mokegyesulet.it.dunestrat.model.Game
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(
    onInputMoves: () -> Unit,
    onStat: () -> Unit,
) {
    val viewModel = viewModel { MainMenuViewModel() }
    val games by viewModel.games.collectAsStateWithLifecycle()
    val gameCount = 0
    var isExpanded by remember { viewModel.isExpanded }
    var selectedGame by remember { mutableStateOf<Game?>(null) }
    val deserts by viewModel.deserts.collectAsStateWithLifecycle()
    val selectedDesert by viewModel.selectedGame.collectAsState()

    Row {
        LazyColumn {
            items(games) { game: Game ->
                Card { Text(game.name) }
            }
        }
        Column(
            Modifier.padding(10.dp).fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Button(
                onClick = { viewModel.onEvent(MainMenuViewModel.Event.CreateGame) },
                modifier = Modifier.width(240.dp)
                    .height(60.dp),

            ) {
                Text(text = "Új játék")
            }
            Text(text = "Létrehozott játékok száma: $gameCount")

            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded },
            ) {
                TextField(
                    label = { Text("Játék kiválasztása") },
                    value = selectedGame?.name ?: "Select a game",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(isExpanded) },
                    modifier = Modifier.menuAnchor(),
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                ) {
                    for (g in games) {
                        DropdownMenuItem(
                            text = { Text(text = g.name) },
                            onClick = {
                                viewModel.onEvent(MainMenuViewModel.Event.ExpandMenu)
                                viewModel.onEvent(MainMenuViewModel.Event.SelectGame(g))
                            },
                        )
                    }
                }
                Text(text = "Selected ID: ${selectedGame?.id ?: "none"}")
            }

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

            ) {
                Image(
                    painter = painterResource(Res.drawable.grid),
                    contentDescription = null,
                    modifier = Modifier.background(Color.Yellow)
                        .size(width = 600.dp, height = 600.dp),
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter = painterResource(Res.drawable.grid),
                        contentDescription = null,
                        modifier = Modifier.size(width = 400.dp, height = 400.dp),

                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Button(
                    onClick = onInputMoves,
                    modifier = Modifier.padding(10.dp)
                        .width(200.dp)
                        .height(50.dp)
                        .align(Alignment.End),
                ) {
                    Text(text = "Lépések felvétele")
                }
                Text(
                    text = "Statisztikák: ",
                    textAlign = TextAlign.Left,
                    modifier = Modifier.width(200.dp),

                )

//            Text(
//                text = "${viewModel.waterAmount}",
//                textAlign = TextAlign.Left,
//            )

                Spacer(
                    modifier = Modifier.height(8.dp)
                        .weight(10f),
                )

                Button(
                    onClick = onStat,
                    modifier = Modifier.padding(10.dp),
                ) {
                    Text(text = "STAT")
                }
            }
        }
    }
}
