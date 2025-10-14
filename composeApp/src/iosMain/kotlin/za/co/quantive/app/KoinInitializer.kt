package za.co.quantive.app

import org.koin.core.context.startKoin
import za.co.quantive.app.di.appModules
import za.co.quantive.shared.config.ConfigurationProvider
import za.co.quantive.shared.config.getPlatformBuildConfig

/**
 * Initializes configuration and Koin for iOS
 * This should be called from the iOS app delegate before creating the UI
 */
fun initKoin() {
    // Initialize configuration provider first
    // This must happen before Koin starts since modules depend on it
    ConfigurationProvider.initialize(getPlatformBuildConfig())

    startKoin {
        // Load all Koin modules
        modules(appModules)
    }
}
