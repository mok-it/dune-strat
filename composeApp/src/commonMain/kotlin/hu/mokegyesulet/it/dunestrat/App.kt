package hu.mokegyesulet.it.dunestrat

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import hu.mokegyesulet.it.dunestrat.navigation.NavGraph
import hu.mokegyesulet.it.dunestrat.ui.AppTheme
import hu.mokegyesulet.it.dunestrat.ui.dark_background
import hu.mokegyesulet.it.dunestrat.ui.light_background
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    Surface(color = if(isSystemInDarkTheme()){
        light_background
    }else{
        dark_background
    },tonalElevation = 5.dp) {
        AppTheme {
            NavGraph()
        }
    }

}
