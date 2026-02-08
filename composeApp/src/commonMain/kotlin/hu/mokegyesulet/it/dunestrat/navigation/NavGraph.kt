package hu.mokegyesulet.it.dunestrat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.mokegyesulet.it.dunestrat.feature.mainmenu.MainMenu
import hu.mokegyesulet.it.dunestrat.feature.testdata.TestDataScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainMenu.route,
    ) {
        composable(Screen.MainMenu.route) {
            MainMenu(
                onPlaceholder1 = {},
                onPlaceholder2 = {},
                onTestData = { navController.navigate(Screen.TestData.route) },
            )
        }

        composable(Screen.TestData.route) { TestDataScreen() }
    }
}
