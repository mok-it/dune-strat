package hu.mokegyesulet.it.dunestrat.feature.mainmenu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MainMenu(
    onPlayerStepInput: () -> Unit,
    onPlaceholder2: () -> Unit,
    onTestData: () -> Unit,
) {
    val viewModel: MainMenuViewModel = viewModel { MainMenuViewModel() }
    val loggedIn by viewModel.isLoggedIn.collectAsState(initial = false)
    if (loggedIn) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Button(
                onClick = onPlayerStepInput,
            ) {
                Text(text = "Player step input")
            }

            Button(
                onClick = onPlaceholder2,
            ) {
                Text(text = "Placeholder 2")
            }

            Button(
                onClick = onTestData,
            ) {
                Text(text = "Test data")
            }

            Button(
                onClick = viewModel::onLogout,
            ) {
                Text(text = "Log out")
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = viewModel::onLogin,
            ) {
                Text(text = "Log in")
            }
        }
    }
}
