package hu.mokegyesulet.it.dunestrat.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun CreatePlayerCard(modifier: Modifier) {
    Card(
        modifier = modifier.padding(20.dp)
    ) {
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
            label = { Text("Fűszer") }
        )

        var pistol = remember { mutableStateOf("0") }
        TextField(
            state = rememberTextFieldState(pistol.value),
            label = { Text("Pisztoly") }
        )

        var lasgun = remember { mutableStateOf("0") }
        TextField(
            state = rememberTextFieldState(lasgun.value),
            label = { Text("Lasgun") }
        )

        var crysknife = remember { mutableStateOf("0") }
        TextField(
            state = rememberTextFieldState(crysknife.value),
            label = { Text("Crysknife") }
        )

        var legio = remember { mutableStateOf("0") }
        TextField(
            state = rememberTextFieldState(legio.value),
            label = { Text("Légio") }
        )

        var startingGameStateID = remember { mutableStateOf("A1") }
        TextField(
            state = rememberTextFieldState(startingGameStateID.value),
            label = { Text("Kezdő mező ID") }
        )
    }
}
