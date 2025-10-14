package za.co.quantive.app.testing

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import za.co.quantive.app.ui.base.BaseViewModel
import za.co.quantive.app.ui.base.UiEffect
import za.co.quantive.app.ui.base.UiEvent
import za.co.quantive.app.ui.base.UiState
import kotlin.test.AfterTest
import kotlin.test.assertEquals

/**
 * Base test class for testing ViewModels
 * Provides utilities for testing state, events, and effects
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseViewModelTest<
    STATE : UiState,
    EVENT : UiEvent,
    EFFECT : UiEffect,
    VM : BaseViewModel<STATE, EVENT, EFFECT>
> : BaseTest() {

    /**
     * The ViewModel under test
     * Must be initialized in the test class
     */
    protected abstract val viewModel: VM

    @AfterTest
    override fun tearDown() {
        super.tearDown()
        // ViewModels are typically scoped to tests, but we could add cleanup here if needed
    }

    /**
     * Helper to assert the current state
     */
    protected fun assertState(expected: STATE) {
        assertEquals(expected, viewModel.uiState.value)
    }

    /**
     * Helper to assert a state property
     */
    protected fun assertState(assertion: (STATE) -> Boolean, message: String = "State assertion failed") {
        assert(assertion(viewModel.uiState.value)) { message }
    }

    /**
     * Test an event and verify the resulting state
     */
    protected fun testEvent(
        event: EVENT,
        expectedState: STATE
    ) = runTest {
        viewModel.onEvent(event)
        advanceUntilIdle()
        assertState(expectedState)
    }

    /**
     * Test an event and verify the resulting effect
     */
    protected fun testEffect(
        event: EVENT,
        expectedEffect: EFFECT
    ) = runTest {
        viewModel.effect.test {
            viewModel.onEvent(event)
            advanceUntilIdle()
            assertEquals(expectedEffect, awaitItem())
        }
    }

    /**
     * Test multiple state changes
     */
    protected fun testStateFlow(
        event: EVENT,
        expectedStates: List<STATE>
    ) = runTest {
        viewModel.uiState.test {
            // Skip initial state
            awaitItem()

            viewModel.onEvent(event)
            advanceUntilIdle()

            expectedStates.forEach { expected ->
                assertEquals(expected, awaitItem())
            }
        }
    }
}
