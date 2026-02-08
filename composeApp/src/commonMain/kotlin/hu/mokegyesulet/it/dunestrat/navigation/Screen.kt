package hu.mokegyesulet.it.dunestrat.navigation

sealed class Screen(
    val route: String,
) {
    data object MainMenu : Screen(route = "main-menu")
    data object TestData : Screen(route = "test-data")
}
