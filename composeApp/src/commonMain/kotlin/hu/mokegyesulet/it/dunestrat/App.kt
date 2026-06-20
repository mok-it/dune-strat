package hu.mokegyesulet.it.dunestrat

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import hu.mokegyesulet.it.dunestrat.navigation.NavGraph
import hu.mokegyesulet.it.dunestrat.ui.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

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
