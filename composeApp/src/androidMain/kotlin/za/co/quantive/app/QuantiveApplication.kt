package za.co.quantive.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import za.co.quantive.app.di.appModules
import za.co.quantive.shared.config.ConfigurationProvider
import za.co.quantive.shared.config.getPlatformBuildConfig

/**
 * Android Application class for Quantive
 * Initializes configuration and Koin dependency injection
 */
class QuantiveApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize configuration provider first
        // This must happen before Koin starts since modules depend on it
        ConfigurationProvider.initialize(getPlatformBuildConfig())

        startKoin {
            // Enable Android-specific logging (only in debug)
            androidLogger(Level.ERROR)

            // Provide Android context
            androidContext(this@QuantiveApplication)

            // Load all Koin modules
            modules(appModules)
        }
    }
}
