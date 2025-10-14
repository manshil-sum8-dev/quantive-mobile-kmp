package za.co.quantive.app.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import za.co.quantive.shared.utils.Result

/**
 * Base class for use cases following Clean Architecture
 * Use cases encapsulate business logic and are called from ViewModels
 *
 * @param P Parameters type for the use case
 * @param R Return type for the use case
 */
abstract class UseCase<in P, R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    /**
     * Execute the use case with parameters
     */
    suspend operator fun invoke(params: P): Result<R> = withContext(dispatcher) {
        execute(params)
    }

    /**
     * Override this method to implement the use case logic
     */
    protected abstract suspend fun execute(params: P): Result<R>
}

/**
 * Base class for use cases that don't require parameters
 */
abstract class NoParamsUseCase<R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    /**
     * Execute the use case without parameters
     */
    suspend operator fun invoke(): Result<R> = withContext(dispatcher) {
        execute()
    }

    /**
     * Override this method to implement the use case logic
     */
    protected abstract suspend fun execute(): Result<R>
}
