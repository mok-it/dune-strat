package hu.mokegyesulet.it.dunestrat

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import hu.mokegyesulet.it.dunestrat.navigation.NavGraph
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        NavGraph()
    }
}
