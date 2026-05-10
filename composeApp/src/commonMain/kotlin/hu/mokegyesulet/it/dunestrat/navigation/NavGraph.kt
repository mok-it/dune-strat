package hu.mokegyesulet.it.dunestrat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.mokegyesulet.it.dunestrat.feature.init.InitScreen
import hu.mokegyesulet.it.dunestrat.feature.mainmenu.MainMenu
import hu.mokegyesulet.it.dunestrat.feature.testdata.TestDataScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainMenu,
    ) {
        composable<Screen.MainMenu> {
            MainMenu(
                onPlaceholder1 = { navController.navigate(Screen.InitScreen) },
                onPlaceholder2 = {},
                onTestData = { navController.navigate(Screen.TestData) },
            )
        }

        composable<Screen.TestData> { TestDataScreen() }
        composable<Screen.InitScreen> {
            InitScreen(onNavigateBack = {
                navController.navigate(Screen.MainMenu) {
                    popUpTo(Screen.MainMenu) { inclusive = true }
                }
            })
        }
    }
}
