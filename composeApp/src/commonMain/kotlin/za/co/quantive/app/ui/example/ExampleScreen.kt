package za.co.quantive.app.ui.example

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.koin.compose.koinInject
import za.co.quantive.app.ui.base.HandleEffects

/**
 * Example screen demonstrating MVI pattern with Compose
 * This is a template showing how to implement screens
 */
@Composable
fun ExampleScreen(
    viewModel: ExampleViewModel = koinInject(),
    navController: NavController = rememberNavController()
) {
    val state by viewModel.uiState.collectAsState()

    // Handle one-time effects
    HandleEffects(viewModel.effect) { effect ->
        when (effect) {
            is ExampleContract.Effect.ShowToast -> {
                // In real implementation, show toast/snackbar
                println("Toast: ${effect.message}")
            }
            is ExampleContract.Effect.NavigateBack -> {
                navController.navigateUp()
            }
        }
    }

    ExampleScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun ExampleScreenContent(
    state: ExampleContract.State,
    onEvent: (ExampleContract.Event) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading...")
            }

            state.error != null -> {
                Text(
                    text = "Error: ${state.error}",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onEvent(ExampleContract.Event.ClearError) }) {
                    Text("Clear Error")
                }
            }

            state.data != null -> {
                Text(
                    text = state.data,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onEvent(ExampleContract.Event.Refresh) }) {
                    Text("Refresh")
                }
            }

            else -> {
                Text("No data loaded")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onEvent(ExampleContract.Event.LoadData) }) {
                    Text("Load Data")
                }
            }
        }
    }
}
