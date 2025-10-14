package za.co.quantive.app.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 * Composable function to handle one-time UI effects
 * Effects are consumed once and trigger side effects like navigation or showing snackbars
 *
 * @param effectFlow The flow of effects to collect
 * @param onEffect Callback to handle each effect
 */
@Composable
fun <T : UiEffect> HandleEffects(
    effectFlow: Flow<T>,
    onEffect: (T) -> Unit
) {
    LaunchedEffect(Unit) {
        effectFlow.collectLatest { effect ->
            onEffect(effect)
        }
    }
}
