package za.co.quantive.app.ui.base

/**
 * Base interface for UI events in MVI pattern
 * All user interactions/events should implement this interface
 */
interface UiEvent

/**
 * Base sealed class for common UI events across the app
 */
sealed class CommonUiEvent : UiEvent {
    /**
     * Retry event for error states
     */
    data object Retry : CommonUiEvent()

    /**
     * Dismiss error event
     */
    data object DismissError : CommonUiEvent()
}
