package za.co.quantive.app.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes using kotlinx.serialization
 * All routes should be serializable for type-safe navigation
 */
@Serializable
sealed interface NavigationRoute {

    /**
     * Landing screen route - showcases Material 3 Expressive design
     */
    @Serializable
    data object Landing : NavigationRoute

    /**
     * Example screen route
     */
    @Serializable
    data object Example : NavigationRoute

    /**
     * Add more routes as features are implemented
     * Example with parameters:
     * @Serializable
     * data class UserDetail(val userId: String) : NavigationRoute
     */
}
