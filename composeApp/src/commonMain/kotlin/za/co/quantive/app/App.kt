package za.co.quantive.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.ui.tooling.preview.Preview
import za.co.quantive.app.navigation.NavigationGraph
import za.co.quantive.app.ui.theme.QuantiveExpressiveTheme

/**
 * Main application composable
 * Sets up the Material 3 Expressive theme and navigation
 */
@Composable
@Preview
fun App() {
    QuantiveExpressiveTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            NavigationGraph(navController = navController)
        }
    }
}