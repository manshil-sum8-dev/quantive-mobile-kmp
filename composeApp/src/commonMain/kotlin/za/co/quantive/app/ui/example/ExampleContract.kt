package za.co.quantive.app.ui.example

import za.co.quantive.app.ui.base.UiEffect
import za.co.quantive.app.ui.base.UiEvent
import za.co.quantive.app.ui.base.UiState

/**
 * Example contract demonstrating MVI pattern
 * This is a template showing how to structure screen contracts
 */
object ExampleContract {

    /**
     * UI State for the example screen
     */
    data class State(
        val isLoading: Boolean = false,
        val data: String? = null,
        val error: String? = null
    ) : UiState

    /**
     * UI Events (user interactions)
     */
    sealed class Event : UiEvent {
        data object LoadData : Event()
        data object Refresh : Event()
        data object ClearError : Event()
    }

    /**
     * UI Effects (one-time side effects)
     */
    sealed class Effect : UiEffect {
        data class ShowToast(val message: String) : Effect()
        data object NavigateBack : Effect()
        // Add more navigation effects as needed
        // data class NavigateToDetail(val id: String) : Effect()
    }
}
