package za.co.quantive.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import za.co.quantive.app.ui.example.ExampleScreen

/**
 * Main navigation graph for the application
 * Defines all navigation routes and their corresponding screens
 *
 * @param navController The navigation controller for the app
 * @param startDestination The initial route to display
 */
@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: NavigationRoute = NavigationRoute.Example
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Example screen
        composable<NavigationRoute.Example> {
            ExampleScreen()
        }

        // Add more screens here as features are implemented
        // Example with parameters:
        // composable<NavigationRoute.UserDetail> { backStackEntry ->
        //     val route = backStackEntry.toRoute<NavigationRoute.UserDetail>()
        //     UserDetailScreen(userId = route.userId)
        // }
    }
}
