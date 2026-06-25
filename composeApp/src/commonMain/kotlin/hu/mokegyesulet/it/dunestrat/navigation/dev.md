To add a new composable to the navigation follow these steps:
- Create a data object/class for it in `Screen.kt`
  - If you don't need additional information from navigation use a data object: `data object YourComponentsName : Screen()`
  - If you need some additional info (e.g. id of an object) use a data class: `data class YourComponentsName(val objectId: Int, ... ): Screen()`
- Create a `composable<Screen.YourComponentsName>` entry in `NavGraph.kt`
  - Add your composable to it
  - You can access your navigation arguments like that:
    ```kotlin 
    composable<Screen.YourComponentsName> { backStackEntry ->
        val navigationObject: Screen.YourComponentsName = backStackEntry.toRoute()
        
        YourComponentsNameScreen(navigationObject.objectid, ...)
    }
    ```
- Fill in the lambdas for navigation
  - Use `navController.navigate(Screen.OtherComponentsName)` to navigate to other components
  - Use `navController.popBackStack()` to navigate to the previously displayed component before your component