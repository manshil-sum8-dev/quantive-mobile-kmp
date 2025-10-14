package za.co.quantive.app.ui.base

/**
 * Base interface for one-time UI effects in MVI pattern
 * Effects are consumed once and don't persist in state
 * Examples: showing snackbars, navigation, showing dialogs
 */
interface UiEffect

/**
 * Base sealed class for common UI effects across the app
 */
sealed class CommonUiEffect : UiEffect {
    /**
     * Show a toast/snackbar message
     */
    data class ShowToast(val message: String) : CommonUiEffect()

    /**
     * Show an error dialog
     */
    data class ShowErrorDialog(val title: String, val message: String) : CommonUiEffect()

    /**
     * Navigate back
     */
    data object NavigateBack : CommonUiEffect()
}
