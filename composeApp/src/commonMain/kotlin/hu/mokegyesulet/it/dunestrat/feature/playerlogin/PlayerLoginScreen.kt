package hu.mokegyesulet.it.dunestrat.feature.playerlogin

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun PlayerLoginScreen() {
    val viewModel = viewModel { PlayerLoginViewModel() }
    val enteredCode by viewModel.code
    Column {
        TextField(value = enteredCode, onValueChange = { newCode: String ->
            viewModel.onEvent(PlayerLoginViewModel.PlayerLoginEvent.CodeChanged(newCode))
        })
        Button(
            onClick = { println("tényleg írtál be kódot!") },
            enabled = viewModel.enabled.value,
        ) { Text("Tovább") }
    }
}

@Preview
@Composable
fun JustAButton() {
    Button(
        onClick = { println("Clicked!!") },
    ) {
        Text("Just a button")
    }
}
