package hu.mokegyesulet.it.dunestrat

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import hu.mokegyesulet.it.dunestrat.navigation.NavGraph
import hu.mokegyesulet.it.dunestrat.ui.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import hu.mokegyesulet.it.dunestrat.feature.init.InitScreen
import hu.mokegyesulet.it.dunestrat.ui.CreatePlayerCard

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
