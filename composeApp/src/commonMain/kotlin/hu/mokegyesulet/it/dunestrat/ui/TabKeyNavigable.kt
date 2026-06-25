package hu.mokegyesulet.it.dunestrat.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type

fun Modifier.tabKeyNavigable(focusManager: FocusManager): Modifier = this.onPreviewKeyEvent {
    if (it.type == KeyEventType.KeyDown && it.key == Key.Tab) {
        focusManager.moveFocus(
            if (it.isShiftPressed) {
                FocusDirection.Previous
            } else {
                FocusDirection.Next
            },
        )
        true
    } else {
        false
    }
}
