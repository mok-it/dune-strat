package hu.mokegyesulet.it.dunestrat.feature.init

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.ui.CreatePlayerCard

@Composable
fun InitScreen() {
    val viewModel = viewModel { InitViewModel() }

    var expanded by remember { mutableStateOf(false) }
    var selectedMap by remember { mutableStateOf("Hexagon – 12 játékos") }
    val mapOptions = listOf("Hexagon – 12 játékos", "Hexagon – 6 játékos")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            OutlinedTextField(
                value = selectedMap,
                onValueChange = {},
                readOnly = true,
                label = { Text("Térkép kiválasztása") },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .clickable() { expanded = true }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                mapOptions.forEach { map ->
                    DropdownMenuItem(
                        text = { Text(map) },
                        onClick = {
                            selectedMap = map
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val playerCount = if (selectedMap.contains("12")) 12 else 6

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for (i in 1..playerCount) {
                item { Text(i.toString() + ". játékos") }
                item {
                    CreatePlayerCard(
                        modifier = Modifier.fillMaxWidth(0.45f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            item {
                Button(
                    onClick = { viewModel.savePlayers() },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Adatok mentése")
                }
            }
        }
    }
}

private fun InitViewModel.savePlayers() {
    TODO("savePlayers függvény hiányos")
}
