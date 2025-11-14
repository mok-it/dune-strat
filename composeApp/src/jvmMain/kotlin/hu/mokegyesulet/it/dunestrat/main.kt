package hu.mokegyesulet.it.dunestrat

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    // Initialize Napier logging for Desktop/JVM
    debugBuild()
    Window(
        onCloseRequest = ::exitApplication,
        title = "dune-strat",
    ) {
        App()
    }
}
