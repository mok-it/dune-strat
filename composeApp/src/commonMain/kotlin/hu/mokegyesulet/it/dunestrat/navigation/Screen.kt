package hu.mokegyesulet.it.dunestrat.navigation

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object MainMenu : Screen()

    @Serializable
    data object TestData : Screen()

    @Serializable
    data object InitScreen : Screen()

    @Serializable
    data class PlayerStepInput(val gameId: Int) : Screen()

    @Serializable
    data class Inventory(val gameId: Int) : Screen()
}
