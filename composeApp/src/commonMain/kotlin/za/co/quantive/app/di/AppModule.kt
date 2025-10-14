package za.co.quantive.app.di

import org.koin.core.module.Module

/**
 * List of all Koin modules for the application
 */
val appModules: List<Module> = listOf(
    networkModule,
    repositoryModule,
    useCaseModule,
    viewModelModule
)
