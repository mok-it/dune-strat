package hu.mokegyesulet.it.dunestrat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hu.mokegyesulet.it.dunestrat.feature.init.InitScreen
import hu.mokegyesulet.it.dunestrat.feature.mainmenu.MainMenu
import hu.mokegyesulet.it.dunestrat.feature.playerstep.PlayerStepInputScreen
import hu.mokegyesulet.it.dunestrat.feature.testdata.TestDataScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    toRoute: NavBackStackEntry.() -> Screen.PlayerStepInput,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainMenu,
    ) {
        composable<Screen.MainMenu> {
            MainMenu(
                onInputMoves = {},
                onStat = {},
            )
        }

        composable<Screen.TestData> { TestDataScreen() }

        composable<Screen.InitScreen> {
            InitScreen(
                onNavigateBack = {
                    navController.navigate(Screen.MainMenu) {
                        popUpTo(Screen.MainMenu) { inclusive = true }
                    }
                },
            )
        }

        composable<Screen.PlayerStepInput> { backStackEntry ->

            val navigationObject: Screen.PlayerStepInput = backStackEntry.toRoute()

            PlayerStepInputScreen(
                gameId = navigationObject.gameId,
                onBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}
