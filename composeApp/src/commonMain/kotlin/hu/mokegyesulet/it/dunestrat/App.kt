package hu.mokegyesulet.it.dunestrat

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import hu.mokegyesulet.it.dunestrat.navigation.NavGraph
import hu.mokegyesulet.it.dunestrat.ui.AppTheme
import hu.mokegyesulet.it.dunestrat.ui.md_theme_dark_background
import hu.mokegyesulet.it.dunestrat.ui.md_theme_light_background
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    Surface(color = if(isSystemInDarkTheme()){
        md_theme_light_background
    }else{
        md_theme_dark_background
    },tonalElevation = 5.dp) {
        AppTheme {
            NavGraph()
        }
    }

}
