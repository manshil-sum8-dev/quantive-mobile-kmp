package za.co.quantive.app.ui.example

import kotlinx.coroutines.test.runTest
import za.co.quantive.app.testing.BaseViewModelTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Example test for ExampleViewModel
 * Demonstrates testing ViewModels with MVI pattern
 */
class ExampleViewModelTest : BaseViewModelTest<
    ExampleContract.State,
    ExampleContract.Event,
    ExampleContract.Effect,
    ExampleViewModel
>() {

    override lateinit var viewModel: ExampleViewModel

    @BeforeTest
    override fun setUp() {
        super.setUp()
        viewModel = ExampleViewModel()
    }

    @Test
    fun `test initial state`() {
        // Given - ViewModel is initialized
        val initialState = viewModel.uiState.value

        // Then
        assertFalse(initialState.isLoading)
        assertNull(initialState.data)
        assertNull(initialState.error)
    }

    @Test
    fun `test load data event sets loading state`() = runTest {
        // When
        viewModel.onEvent(ExampleContract.Event.LoadData)

        // Then - State should be loading
        // Note: In a real test, you might want to collect states to verify the loading state
        // For this example, we're just checking the final state after delay
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `test refresh event clears error and loads data`() = runTest {
        // Given - ViewModel has an error
        viewModel.onEvent(ExampleContract.Event.LoadData)

        // When
        viewModel.onEvent(ExampleContract.Event.Refresh)

        // Then
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `test clear error event clears error`() {
        // Given - Set an error state (in a real scenario, this would come from a failed operation)
        // For this test, we'll just verify the event is handled

        // When
        viewModel.onEvent(ExampleContract.Event.ClearError)

        // Then - Error should be null
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `test state changes correctly`() {
        // Given
        val initialState = ExampleContract.State()

        // When - Load data
        viewModel.onEvent(ExampleContract.Event.LoadData)

        // Then - Loading state is set
        val loadingState = viewModel.uiState.value
        assertTrue(loadingState.isLoading)
    }
}
