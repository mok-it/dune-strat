package hu.mokegyesulet.it.dunestrat.feature.mainmenu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dune_strat.composeapp.generated.resources.Res
import dune_strat.composeapp.generated.resources.grid
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(
    onInputMoves: () -> Unit,
    onStat: () -> Unit,
) {
    val viewModel = viewModel { MainMenuViewModel() }
    val gameCount by viewModel.gameCount
    val games by viewModel.games.collectAsState(emptyList())
    var showMenu by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }
    var gameOpened by remember { mutableStateOf("") }
    Row {
        Column(
            Modifier.padding(10.dp),
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
                onExpandedChange = { isExpanded = it },
            ) {
                TextField(
                    label = { Text("Játék kiválasztása") },
                    value = gameOpened,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    // modifier = Modifier.menuAnchor(),
                    modifier = Modifier.menuAnchor(
                        ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    ),

                )
                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },

                ) {
                    DropdownMenuItem(
                        text = { Text(text = "Játék 1") },
                        onClick = {
                            isExpanded = false
                            gameOpened = "Játék 1"
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Játék 2") },
                        onClick = {
                            isExpanded = false
                            gameOpened = "Játék 2"
                        },
                    )
                    DropdownMenuItem(
                        text = { Text(text = "Játék 3") },
                        onClick = {
                            isExpanded = false
                            gameOpened = "Játék 3"
                        },
                    )
                }
            }
        }

        Column(
            Modifier.weight(1f)
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,

        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(Res.drawable.grid),
                    contentDescription = null,
                    modifier = Modifier.background(Color.Yellow)
                        .size(width = 600.dp, height = 600.dp),

                )
            }
        }

        Column(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End,

        ) {
            Button(
                onClick = onInputMoves,
                modifier = Modifier.padding(10.dp)
                    .width(200.dp)
                    .height(50.dp),
            ) {
                Text(text = "Lépések felvétele")
            }
            Text(
                text = "Statisztikák: ",
                textAlign = TextAlign.Left,
                modifier = Modifier.width(200.dp),

            )

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
