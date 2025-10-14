package za.co.quantive.app.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder

/**
 * Extension functions for easier navigation
 */

/**
 * Navigate to a route with optional navigation options
 */
fun NavController.navigateTo(
    route: NavigationRoute,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route) {
        builder()
    }
}

/**
 * Navigate and clear the back stack
 */
fun NavController.navigateAndClearBackStack(route: NavigationRoute) {
    navigate(route) {
        popUpTo(0) { inclusive = true }
    }
}

/**
 * Navigate and pop up to a specific route
 */
fun NavController.navigateAndPopUpTo(
    route: NavigationRoute,
    popUpToRoute: NavigationRoute,
    inclusive: Boolean = false
) {
    navigate(route) {
        popUpTo(popUpToRoute) { this.inclusive = inclusive }
    }
}

/**
 * Navigate with single top behavior (avoid multiple instances)
 */
fun NavController.navigateSingleTop(route: NavigationRoute) {
    navigate(route) {
        launchSingleTop = true
    }
}
