package za.co.quantive.app.di

import org.koin.dsl.module
import za.co.quantive.app.ui.example.ExampleViewModel

/**
 * Koin module for ViewModels
 * ViewModels will be added here as features are implemented
 */
val viewModelModule = module {
    // Example ViewModel demonstrating MVI pattern
    factory { ExampleViewModel() }

    // Additional ViewModels will be registered here
}
