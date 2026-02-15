package hu.mokegyesulet.it.dunestrat.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun MainMenu(
    onPlaceholder1: () -> Unit,
    onPlaceholder2: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(
            onClick = onPlaceholder1,
        ) {
            Text(text = "Placeholder 1",
                style = MaterialTheme.typography.titleLarge)
        }

        Button(
            onClick = onPlaceholder2,
        ) {
            Text(text = "Placeholder 2",
                style = MaterialTheme.typography.bodyMedium)
        }
    }
}
