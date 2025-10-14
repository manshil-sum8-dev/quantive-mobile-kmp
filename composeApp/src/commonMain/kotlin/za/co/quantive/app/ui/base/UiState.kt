package za.co.quantive.app.ui.base

/**
 * Base interface for UI state in MVI pattern
 * All screen states should implement this interface
 */
interface UiState

/**
 * Base sealed class for common UI states across the app
 */
sealed class CommonUiState : UiState {
    /**
     * Initial state when the screen is first loaded
     */
    data object Initial : CommonUiState()

    /**
     * Loading state while data is being fetched
     */
    data object Loading : CommonUiState()

    /**
     * Error state with an error message
     */
    data class Error(val message: String) : CommonUiState()
}
