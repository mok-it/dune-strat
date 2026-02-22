package hu.mokegyesulet.it.dunestrat.feature

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hu.mokegyesulet.it.dunestrat.ui.DuneFontFamily
import hu.mokegyesulet.it.dunestrat.ui.hexagonalRectangle

@Composable
fun MainMenu(onPlaceholder1: () -> Unit, onPlaceholder2: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Button(
            onClick = onPlaceholder1,
            shape = hexagonalRectangle,
            border = BorderStroke(1.dp, colorScheme.outline),
            colors = ButtonColors(
                colorScheme.primary,
                colorScheme.onPrimary,
                colorScheme.secondary,
                colorScheme.onSecondary,
            ),
        ) {
            Text(
                text = "aeo áéőè",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Button(
            onClick = onPlaceholder2,
        ) {
            Text(
                text = "Placeholder 2",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = DuneFontFamily(),
            )
        }
        Text(
            text = "abc ABC",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Light,
        )
        Text(
            text = "abc ABC",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "abc ABC",
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = "abc ABC",
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = "abc ABC",
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
