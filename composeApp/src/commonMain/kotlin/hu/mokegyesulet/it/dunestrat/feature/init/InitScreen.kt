package hu.mokegyesulet.it.dunestrat.feature.init

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.mokegyesulet.it.dunestrat.ui.CreatePlayerCard

@Composable
fun InitScreen() {
    val viewModel = viewModel { InitViewModel() }

    LazyColumn {
        for (i in 1..12) {
            item { CreatePlayerCard() }
        }
    }

}
