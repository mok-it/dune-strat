package hu.mokegyesulet.it.dunestrat.ui

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun CreatePlayerCard() {
    Card() {
        Text("Játékos:")
        var playerID = remember { mutableStateOf("Jozsi") }
        TextField(
            state = rememberTextFieldState(playerID.value),
            label = { Text("Játékos ID") }
        )

        var water = remember { mutableStateOf("0") }
        TextField(
            state = rememberTextFieldState(water.value),
            label = { Text("Víz") }
        )

        var spice = remember { mutableStateOf("0") }
        TextField(
            state = rememberTextFieldState(spice.value),
            label = { Text("Spice") }
        )

        var pistol = remember { mutableStateOf("0") }
        TextField(
            state = rememberTextFieldState(pistol.value),
            label = { Text("Spice") }
        )

        var lasgun = remember { mutableStateOf("0") }
        TextField(
            state = rememberTextFieldState(lasgun.value),
            label = { Text("Spice") }
        )

        var crysknife = remember { mutableStateOf("0") }
        TextField(
            state = rememberTextFieldState(crysknife.value),
            label = { Text("Spice") }
        )

        var legio = remember { mutableStateOf("0") }
        TextField(
            state = rememberTextFieldState(legio.value),
            label = { Text("Spice") }
        )

        var startingGameStateID = remember { mutableStateOf("A1") }
        TextField(
            state = rememberTextFieldState(startingGameStateID.value),
            label = { Text("Kezdő mező ID") }
        )
    }
}
