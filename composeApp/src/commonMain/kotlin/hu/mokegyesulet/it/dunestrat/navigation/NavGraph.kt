package hu.mokegyesulet.it.dunestrat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import hu.mokegyesulet.it.dunestrat.feature.init.InitScreen
import hu.mokegyesulet.it.dunestrat.feature.inventory.InventoryScreen
import hu.mokegyesulet.it.dunestrat.feature.mainmenu.MainMenu
import hu.mokegyesulet.it.dunestrat.feature.playerstep.PlayerStepInputScreen
import hu.mokegyesulet.it.dunestrat.feature.testdata.TestDataScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.MainMenu,
    ) {
        composable<Screen.MainMenu> {
            MainMenu(
                onInputMovesClick = { gameId ->
                    navController.navigate(Screen.PlayerStepInput(gameId))
                },
                onNewGameClick = {
                    navController.navigate(Screen.InitScreen)
                },
                onInventoryClick = { gameId ->
                    navController.navigate(Screen.Inventory(gameId))
                },
            )
        }

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
                onRunTurn = {
                    navController.popBackStack()
                    navController.navigate(Screen.Inventory(navigationObject.gameId))
                },
            )
        }
        composable<Screen.Inventory> { backStackEntry ->
            val navigationObject: Screen.Inventory = backStackEntry.toRoute()
            InventoryScreen(
                gameId = navigationObject.gameId,
                onBack = { navController.popBackStack() },
            )
        }
        composable<Screen.TestData> { TestDataScreen(onTestBack = { navController.navigateUp() }) }
    }
}
