package hu.mokegyesulet.it.dunestrat.feature.testdata

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestDataScreen(onTestBack: () -> Unit) {
    val viewModel = viewModel { TestDataViewModel() }
    val deserts by viewModel.deserts.collectAsStateWithLifecycle()
    val games by viewModel.games.collectAsStateWithLifecycle()
    val gameStates by viewModel.gameStates.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Teszt adatok") },
                navigationIcon = {
                    IconButton(onClick = onTestBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Vissza")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
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
}
