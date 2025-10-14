package za.co.quantive.app.ui.example

import za.co.quantive.app.ui.base.BaseViewModel

/**
 * Example ViewModel demonstrating MVI pattern
 * This is a template showing how to implement ViewModels
 */
class ExampleViewModel : BaseViewModel<
        ExampleContract.State,
        ExampleContract.Event,
        ExampleContract.Effect
        >(
    initialState = ExampleContract.State()
) {

    override fun onEvent(event: ExampleContract.Event) {
        when (event) {
            is ExampleContract.Event.LoadData -> loadData()
            is ExampleContract.Event.Refresh -> refresh()
            is ExampleContract.Event.ClearError -> clearError()
        }
    }

    private fun loadData() {
        setState { copy(isLoading = true, error = null) }

        launch {
            // Simulate data loading
            // In real implementation, call use case here
            kotlinx.coroutines.delay(1000)

            setState {
                copy(
                    isLoading = false,
                    data = "Example data loaded successfully"
                )
            }

            sendEffect(ExampleContract.Effect.ShowToast("Data loaded!"))
        }
    }

    private fun refresh() {
        setState { copy(isLoading = true, error = null, data = null) }

        launch {
            // Simulate refresh
            kotlinx.coroutines.delay(1000)

            setState {
                copy(
                    isLoading = false,
                    data = "Refreshed data"
                )
            }
        }
    }

    private fun clearError() {
        setState { copy(error = null) }
    }
}
