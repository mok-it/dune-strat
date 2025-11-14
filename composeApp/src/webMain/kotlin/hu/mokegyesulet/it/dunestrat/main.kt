package hu.mokegyesulet.it.dunestrat

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // Initialize Napier logging for Web/JS
    debugBuild()
    ComposeViewport {
        App()
    }
}
