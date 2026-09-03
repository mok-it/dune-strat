package hu.mokegyesulet.it.dunestrat

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import hu.mokegyesulet.it.dunestrat.navigation.NavGraph
import hu.mokegyesulet.it.dunestrat.ui.AppTheme

@Composable
@Preview
fun App() {
    AppTheme {
        Surface(
            color = colorScheme.background,
        ) {
            NavGraph()
        }
    }
}
