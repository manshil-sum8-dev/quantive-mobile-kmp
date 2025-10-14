package za.co.quantive.app

import androidx.compose.ui.window.ComposeUIViewController

/**
 * Main view controller for iOS
 * Koin should be initialized via initKoin() before calling this
 */
fun MainViewController() = ComposeUIViewController { App() }