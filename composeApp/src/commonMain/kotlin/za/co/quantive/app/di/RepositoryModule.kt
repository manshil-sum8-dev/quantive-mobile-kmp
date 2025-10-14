package za.co.quantive.app.di

import org.koin.dsl.module
import za.co.quantive.app.data.datasource.AuthRemoteDataSource
import za.co.quantive.app.data.datasource.AuthRemoteDataSourceImpl
import za.co.quantive.app.data.repository.AuthRepositoryImpl
import za.co.quantive.app.domain.repository.AuthRepository

/**
 * Koin module for Repositories and Data Sources
 * Repositories will be added here as features are implemented
 */
val repositoryModule = module {
    // Data Sources
    single<AuthRemoteDataSource> {
        AuthRemoteDataSourceImpl(authApiClient = get())
    }

    // Repositories
    single<AuthRepository> {
        AuthRepositoryImpl(remoteDataSource = get())
    }

    // Add more repositories and data sources here as needed
}
