package za.co.quantive.app.di

import io.ktor.client.*
import org.koin.dsl.module
import za.co.quantive.shared.config.AppConfig
import za.co.quantive.shared.config.BuildConfig
import za.co.quantive.shared.config.ConfigurationProvider
import za.co.quantive.shared.network.ApiConfig
import za.co.quantive.shared.network.HttpClientFactory
import za.co.quantive.shared.network.api.AuthApiClient

/**
 * Koin module for network dependencies
 * Integrates with the centralized configuration system
 */
val networkModule = module {

    // Build Configuration (from platform)
    single<BuildConfig> {
        ConfigurationProvider.buildConfig
    }

    // App Configuration (environment-specific)
    single<AppConfig> {
        ConfigurationProvider.appConfig
    }

    // API Configuration (created from AppConfig)
    single<ApiConfig> {
        ApiConfig.fromAppConfig(get())
    }

    // HTTP Client
    single<HttpClient> {
        HttpClientFactory.create(
            config = get()
        )
    }

    // API Clients
    single<AuthApiClient> {
        AuthApiClient(
            httpClient = get(),
            config = get()
        )
    }
}
