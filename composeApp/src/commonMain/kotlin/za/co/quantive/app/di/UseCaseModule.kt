package za.co.quantive.app.di

import org.koin.dsl.module
import za.co.quantive.app.domain.usecase.LoginUseCase

/**
 * Koin module for Use Cases
 * Use cases will be added here as features are implemented
 */
val useCaseModule = module {
    // Auth Use Cases
    factory { LoginUseCase(authRepository = get()) }

    // Add more use cases here as needed
}
