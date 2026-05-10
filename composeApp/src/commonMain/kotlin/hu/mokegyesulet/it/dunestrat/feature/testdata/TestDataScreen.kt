package hu.mokegyesulet.it.dunestrat.feature.testdata

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TestDataScreen() {
    val viewModel = viewModel { TestDataViewModel() }
    val deserts by viewModel.deserts.collectAsStateWithLifecycle()
    val games by viewModel.games.collectAsStateWithLifecycle()
    val gameStates by viewModel.gameStates.collectAsStateWithLifecycle()
    Column {
        Text(
            text = "Deserts",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.headlineMedium,
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(deserts) {
                Text(text = it.id.toString())
            }
        }
        Text(
            text = "Games",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.headlineMedium,
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(games) {
                Text(text = it.id.toString())
            }
        }
        Text(
            text = "Game states",
            modifier = Modifier.padding(8.dp),
            style = MaterialTheme.typography.headlineMedium,
        )
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(gameStates) {
                Text(text = it.id.toString())
            }
        }
        Button(onClick = { viewModel.initDesert() }) { Text("Init test desert") }
        Button(onClick = { viewModel.initGame(deserts.last()) }) { Text("Init test game") }
        Button(onClick = {
            viewModel.initGameState(deserts.last(), games.last())
        }) { Text("Init test gamestate") }
        Button(onClick = {
            viewModel.initPlayerSteps(gameStates.last())
        }) { Text("Init test steps") }
    }
}
