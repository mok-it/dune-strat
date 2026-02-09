package hu.mokegyesulet.it.dunestrat.feature.playerstep

import androidx.compose.foundation.layout.*
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PlayerStepInputScreen() {
    val viewModel = viewModel { PlayerStepInputViewModel() }

    val tabIndex by viewModel.tabIndex

    val game by viewModel.game
    val playerIds = game.teams.map { it.playerId }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            PrimaryScrollableTabRow(
                selectedTabIndex = tabIndex,
                modifier = Modifier.fillMaxWidth(),
            ) {
                playerIds.forEachIndexed { index, playerId ->
                    Tab(
                        selected = index == tabIndex,
                        onClick = {
                            viewModel.onEvent(PlayerStepInputViewModel.Event.TabSelected(index))
                        },
                        text = {
                            Text(text = playerId)
                        },
                    )
                }
            }
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues).fillMaxSize(),
        ) {
            playerIds.forEachIndexed { index, playerId ->
                if (index == tabIndex) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text(text = "$playerId selected")
                    }
                }
            }
        }
    }
}
