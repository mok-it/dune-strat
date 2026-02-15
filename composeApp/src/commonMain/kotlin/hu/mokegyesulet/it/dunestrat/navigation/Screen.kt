package hu.mokegyesulet.it.dunestrat.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    data object MainMenu : Screen()
    data object TestData : Screen()
}
