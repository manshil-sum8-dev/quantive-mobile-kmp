package za.co.quantive.app.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel implementing MVI pattern
 *
 * @param STATE The UI state type
 * @param EVENT The UI event type (user interactions)
 * @param EFFECT The UI effect type (one-time side effects)
 */
abstract class BaseViewModel<STATE : UiState, EVENT : UiEvent, EFFECT : UiEffect>(
    initialState: STATE
) : ViewModel() {

    // State
    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<STATE> = _uiState.asStateFlow()

    // Effects (one-time events)
    private val _effect = Channel<EFFECT>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    /**
     * Current state value
     */
    protected val currentState: STATE
        get() = _uiState.value

    /**
     * Handle user events/interactions
     */
    abstract fun onEvent(event: EVENT)

    /**
     * Update the UI state
     */
    protected fun setState(reduce: STATE.() -> STATE) {
        _uiState.value = currentState.reduce()
    }

    /**
     * Send a one-time effect
     */
    protected fun sendEffect(effect: EFFECT) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    /**
     * Execute a suspend function in viewModelScope
     */
    protected fun launch(block: suspend () -> Unit) {
        viewModelScope.launch {
            block()
        }
    }
}
