# MVI/MVVM+ Architecture

This directory contains the base classes and utilities for implementing MVI (Model-View-Intent) pattern with MVVM+ in the Quantive mobile app.

## Overview

The architecture follows these principles:
- **Unidirectional Data Flow**: Events flow from UI → ViewModel → State → UI
- **Single Source of Truth**: State is the single source of truth for UI
- **Immutability**: States are immutable data classes
- **Separation of Concerns**: Clear separation between UI, business logic, and data

## Core Components

### 1. UiState
Represents the current state of the UI. All screen states should implement this interface.

```kotlin
data class MyScreenState(
    val isLoading: Boolean = false,
    val data: List<Item> = emptyList(),
    val error: String? = null
) : UiState
```

### 2. UiEvent
Represents user interactions/events. These are dispatched from the UI to the ViewModel.

```kotlin
sealed class MyScreenEvent : UiEvent {
    data object LoadData : MyScreenEvent()
    data class ItemClicked(val id: String) : MyScreenEvent()
}
```

### 3. UiEffect
Represents one-time side effects that don't belong in state (navigation, toast messages, etc.).

```kotlin
sealed class MyScreenEffect : UiEffect {
    data class ShowToast(val message: String) : MyScreenEffect()
    data class NavigateToDetail(val id: String) : MyScreenEffect()
}
```

### 4. BaseViewModel
Base class for all ViewModels implementing the MVI pattern.

```kotlin
class MyViewModel : BaseViewModel<MyScreenState, MyScreenEvent, MyScreenEffect>(
    initialState = MyScreenState()
) {
    override fun onEvent(event: MyScreenEvent) {
        when (event) {
            is MyScreenEvent.LoadData -> loadData()
            is MyScreenEvent.ItemClicked -> handleItemClick(event.id)
        }
    }

    private fun loadData() {
        setState { copy(isLoading = true) }
        // Load data...
    }
}
```

### 5. Screen Composable
Screens observe state and effects, and dispatch events.

```kotlin
@Composable
fun MyScreen(viewModel: MyViewModel = koinViewModel()) {
    val state by viewModel.uiState.collectAsState()

    HandleEffects(viewModel.effect) { effect ->
        when (effect) {
            is MyScreenEffect.ShowToast -> showToast(effect.message)
            is MyScreenEffect.NavigateToDetail -> navigate(effect.id)
        }
    }

    MyScreenContent(
        state = state,
        onEvent = viewModel::onEvent
    )
}
```

## Architecture Layers

### Presentation Layer (UI)
- **Screens**: Composable functions that render UI
- **ViewModels**: Manage UI state and business logic
- **Contracts**: Define State, Event, and Effect for each screen

### Domain Layer
- **Use Cases**: Encapsulate business logic
- **Repositories**: Abstract interfaces for data access
- **Models**: Domain-specific data models

### Data Layer
- **Repositories Impl**: Concrete implementations of repository interfaces
- **API Clients**: Network data sources
- **Local Storage**: Database/cache data sources

## Best Practices

1. **Keep ViewModels Pure**: Don't pass Android/iOS-specific types to ViewModels
2. **Immutable State**: Always use data classes and copy() for state updates
3. **Handle Errors Gracefully**: Always handle error states in UI
4. **One-Time Events**: Use Effects for navigation, toasts, etc. (not State)
5. **Single Responsibility**: Each screen should have one ViewModel
6. **Testing**: ViewModels should be easily testable with mock repositories

## Example Structure

```
ui/
├── base/              # Base classes (this directory)
├── example/           # Example screen demonstrating MVI
│   ├── ExampleContract.kt
│   ├── ExampleViewModel.kt
│   └── ExampleScreen.kt
└── features/          # Feature-specific screens
    ├── auth/
    ├── dashboard/
    └── settings/
```

## References

- [MVI Pattern](https://hannesdorfmann.com/android/mosby3-mvi-1/)
- [Unidirectional Data Flow](https://developer.android.com/topic/architecture/ui-layer#udf)
- [Compose State Management](https://developer.android.com/jetpack/compose/state)
