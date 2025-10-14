# Testing Infrastructure

This directory contains the testing infrastructure for the Quantive mobile application.

## Overview

The testing infrastructure is built on modern testing libraries and follows best practices for testing Kotlin Multiplatform and Compose Multiplatform applications.

## Testing Libraries

### Core Testing
- **kotlin-test** - Kotlin's built-in testing framework
- **kotlinx-coroutines-test** - Testing utilities for coroutines
- **JUnit 4** - Test runner for JVM targets

### Specialized Testing
- **Turbine** - Testing library for Kotlin Flows
- **Ktor Client Mock** - Mock HTTP client for testing network calls
- **Koin Test** - Testing utilities for dependency injection

## Test Structure

```
commonTest/
├── kotlin/
│   └── za.co.quantive.app/
│       ├── testing/               # Testing infrastructure
│       │   ├── BaseTest.kt        # Base test class
│       │   ├── BaseViewModelTest.kt    # ViewModel testing utilities
│       │   ├── BaseRepositoryTest.kt   # Repository testing utilities
│       │   ├── KoinTestHelper.kt  # Koin testing helper
│       │   ├── TestData.kt        # Test data factories
│       │   ├── mock/              # Mock implementations
│       │   │   └── MockAuthRepository.kt
│       │   └── README.md          # This file
│       ├── domain/                # Use case tests
│       │   └── usecase/
│       │       └── LoginUseCaseTest.kt
│       ├── data/                  # Repository tests
│       │   └── repository/
│       │       └── AuthRepositoryImplTest.kt
│       └── ui/                    # ViewModel tests
│           └── example/
│               └── ExampleViewModelTest.kt
```

## Base Test Classes

### 1. BaseTest

Base class for tests that use coroutines.

```kotlin
class MyTest : BaseTest() {

    @Test
    fun myTest() = runTest {
        // Test code with coroutines
    }
}
```

**Features:**
- Sets up test dispatchers
- Handles cleanup after tests
- Provides `testDispatcher` for controlling coroutine execution

### 2. BaseViewModelTest

Base class for testing ViewModels with MVI pattern.

```kotlin
class MyViewModelTest : BaseViewModelTest<
    MyContract.State,
    MyContract.Event,
    MyContract.Effect,
    MyViewModel
>() {
    override lateinit var viewModel: MyViewModel

    @BeforeTest
    override fun setUp() {
        super.setUp()
        viewModel = MyViewModel()
    }

    @Test
    fun testEvent() {
        testEvent(
            event = MyContract.Event.Load,
            expectedState = MyContract.State(isLoading = true)
        )
    }
}
```

**Features:**
- Test state changes with `assertState()`
- Test events with `testEvent()`
- Test effects with `testEffect()`
- Test state flow with `testStateFlow()`

### 3. BaseRepositoryTest

Base class for testing repositories with mock HTTP clients.

```kotlin
class MyRepositoryTest : BaseRepositoryTest() {

    @Test
    fun testApiCall() = runTest {
        val mockClient = createMockHttpClient { request ->
            mockSuccessResponse(
                content = """{"data": "value"}"""
            )
        }

        // Use mockClient in repository
    }
}
```

**Features:**
- Create mock HTTP clients with `createMockHttpClient()`
- Mock success responses with `mockSuccessResponse()`
- Mock error responses with `mockErrorResponse()`

## Writing Tests

### Testing Use Cases

Use cases contain business logic and validation.

```kotlin
class LoginUseCaseTest : BaseTest() {

    private lateinit var mockRepository: MockAuthRepository
    private lateinit var useCase: LoginUseCase

    @BeforeTest
    override fun setUp() {
        super.setUp()
        mockRepository = MockAuthRepository()
        useCase = LoginUseCase(mockRepository)
    }

    @Test
    fun `test valid login returns success`() = runTest {
        // Given
        val params = LoginUseCase.Params("test@test.com", "password")

        // When
        val result = useCase(params)

        // Then
        assertTrue(result is Result.Success)
    }

    @Test
    fun `test empty email returns error`() = runTest {
        // Given
        val params = LoginUseCase.Params("", "password")

        // When
        val result = useCase(params)

        // Then
        assertTrue(result is Result.Error)
        assertEquals("Please enter your email address", (result as Result.Error).message)
    }
}
```

### Testing ViewModels

ViewModels manage UI state using the MVI pattern.

```kotlin
class LoginViewModelTest : BaseViewModelTest<
    LoginContract.State,
    LoginContract.Event,
    LoginContract.Effect,
    LoginViewModel
>() {

    private lateinit var mockLoginUseCase: LoginUseCase
    override lateinit var viewModel: LoginViewModel

    @BeforeTest
    override fun setUp() {
        super.setUp()
        mockLoginUseCase = // create or inject mock
        viewModel = LoginViewModel(mockLoginUseCase)
    }

    @Test
    fun `test initial state is correct`() {
        assertState(LoginContract.State())
    }

    @Test
    fun `test login event updates state to loading`() = runTest {
        // When
        viewModel.onEvent(LoginContract.Event.Login("test@test.com", "password"))

        // Then
        assertState { it.isLoading }
    }

    @Test
    fun `test successful login shows success effect`() {
        testEffect(
            event = LoginContract.Event.Login("test@test.com", "password"),
            expectedEffect = LoginContract.Effect.NavigateToHome
        )
    }
}
```

### Testing Repositories

Repositories coordinate data sources and provide data to the domain layer.

```kotlin
class AuthRepositoryImplTest : BaseRepositoryTest() {

    private lateinit var repository: AuthRepositoryImpl

    @Test
    fun `test login success`() = runTest {
        // Given
        val expectedResponse = TestData.createAuthResponse()

        val mockClient = createMockHttpClient { request ->
            mockSuccessResponse(
                content = Json.encodeToString(expectedResponse)
            )
        }

        val apiClient = AuthApiClient(mockClient, testConfig)
        val dataSource = AuthRemoteDataSourceImpl(apiClient)
        repository = AuthRepositoryImpl(dataSource)

        // When
        val result = repository.login("test@test.com", "password")

        // Then
        assertTrue(result is Result.Success)
    }

    @Test
    fun `test login network error`() = runTest {
        // Given
        val mockClient = createMockHttpClient { request ->
            mockErrorResponse(
                content = """{"error": "Network error"}""",
                status = HttpStatusCode.InternalServerError
            )
        }

        val apiClient = AuthApiClient(mockClient, testConfig)
        val dataSource = AuthRemoteDataSourceImpl(apiClient)
        repository = AuthRepositoryImpl(dataSource)

        // When
        val result = repository.login("test@test.com", "password")

        // Then
        assertTrue(result is Result.Error)
    }
}
```

### Testing with Dependency Injection

Use `KoinTestHelper` for tests that need DI.

```kotlin
class MyFeatureTest : BaseTest(), KoinTestHelper {

    @BeforeTest
    override fun setUp() {
        super.setUp()
        startKoinForTest(
            module {
                single<AuthRepository> { MockAuthRepository() }
                factory { LoginUseCase(get()) }
            }
        )
    }

    @AfterTest
    override fun tearDown() {
        super.tearDown()
        stopKoinAfterTest()
    }

    @Test
    fun testWithDI() {
        val useCase: LoginUseCase = get()
        // Test with injected dependencies
    }
}
```

## Test Data Factory

Use `TestData` to create consistent test objects:

```kotlin
// Create a test user
val user = TestData.createUser(
    email = "test@test.com",
    firstName = "Test"
)

// Create a test auth response
val authResponse = TestData.createAuthResponse(
    user = user,
    accessToken = "test-token"
)
```

## Mock Implementations

Mock implementations allow controlling behavior in tests.

### MockAuthRepository

```kotlin
val mockRepository = MockAuthRepository()

// Set up success scenario
mockRepository.shouldReturnError = false

// Set up error scenario
mockRepository.shouldReturnError = true
mockRepository.errorMessage = "Custom error message"

// Reset to initial state
mockRepository.reset()
```

## Testing Flows

Use Turbine to test Kotlin Flows:

```kotlin
@Test
fun testStateFlow() = runTest {
    viewModel.uiState.test {
        // Skip initial state
        awaitItem()

        // Trigger event
        viewModel.onEvent(SomeEvent)

        // Assert next state
        val state = awaitItem()
        assertEquals(expected, state)
    }
}

@Test
fun testEffectFlow() = runTest {
    viewModel.effect.test {
        // Trigger event
        viewModel.onEvent(SomeEvent)

        // Assert effect is emitted
        val effect = awaitItem()
        assertTrue(effect is MyEffect.ShowToast)
    }
}
```

## Testing Coroutines

Use `kotlinx-coroutines-test` utilities:

```kotlin
@Test
fun testWithDelay() = runTest {
    // Start coroutine
    val deferred = async {
        delay(1000)
        "result"
    }

    // Fast-forward time
    advanceTimeBy(1000)

    // Assert result
    assertEquals("result", deferred.await())
}

@Test
fun testAllCoroutinesComplete() = runTest {
    // Start multiple coroutines
    launch { delay(100) }
    launch { delay(200) }

    // Wait for all coroutines to complete
    advanceUntilIdle()

    // All coroutines are now complete
}
```

## Best Practices

### 1. Test Naming
Use descriptive test names that explain the scenario:

```kotlin
// Good
@Test
fun `test login with valid credentials returns success`()

@Test
fun `test login with empty email returns validation error`()

// Avoid
@Test
fun testLogin()
```

### 2. Test Structure
Follow the Given-When-Then pattern:

```kotlin
@Test
fun testSomething() = runTest {
    // Given - Set up test data and conditions
    val data = TestData.createUser()

    // When - Execute the code under test
    val result = useCase(data)

    // Then - Assert the expected outcome
    assertTrue(result is Result.Success)
}
```

### 3. Test Independence
Each test should be independent and not rely on other tests:

```kotlin
@BeforeTest
fun setUp() {
    // Set up fresh state for each test
    mockRepository.reset()
}

@AfterTest
fun tearDown() {
    // Clean up after each test
    stopKoinAfterTest()
}
```

### 4. Test One Thing
Each test should verify one specific behavior:

```kotlin
// Good - Tests one validation rule
@Test
fun `test empty email returns error`()

@Test
fun `test invalid email format returns error`()

// Avoid - Tests multiple things
@Test
fun `test email validation`()
```

### 5. Mock External Dependencies
Always mock external dependencies like network, database, etc.:

```kotlin
// Good
val mockClient = createMockHttpClient { /* ... */ }

// Avoid making real network calls in tests
```

## Running Tests

### Run all tests
```bash
./gradlew :composeApp:allTests
```

### Run specific test class
```bash
./gradlew :composeApp:testDebugUnitTest --tests "*.LoginUseCaseTest"
```

### Run with test report
```bash
./gradlew :composeApp:testDebugUnitTest --tests "*" --info
```

## Test Coverage

To generate test coverage reports:

```bash
./gradlew :composeApp:testDebugUnitTestCoverage
```

Coverage reports will be generated in:
```
composeApp/build/reports/coverage/
```

## Common Issues

### Issue: Unresolved reference to test dependencies
**Solution:** Ensure test dependencies are in `commonTest.dependencies` in build.gradle.kts

### Issue: Tests hang indefinitely
**Solution:** Make sure to use `runTest` for tests with coroutines and `advanceUntilIdle()` to complete all coroutines

### Issue: Flow collection never completes
**Solution:** Use Turbine's `test { }` function and properly handle flow collection

### Issue: Koin already started error
**Solution:** Call `stopKoinAfterTest()` in `@AfterTest` to clean up between tests

## Example Test Suite

A complete example showing all testing patterns:

```kotlin
class FeatureTest : BaseViewModelTest<State, Event, Effect, ViewModel>(),
                     KoinTestHelper {

    private lateinit var mockRepository: MockRepository
    override lateinit var viewModel: ViewModel

    @BeforeTest
    override fun setUp() {
        super.setUp()

        mockRepository = MockRepository()

        startKoinForTest(
            module {
                single<Repository> { mockRepository }
                factory { UseCase(get()) }
            }
        )

        viewModel = ViewModel(get())
    }

    @AfterTest
    override fun tearDown() {
        super.tearDown()
        stopKoinAfterTest()
        mockRepository.reset()
    }

    @Test
    fun `test feature behavior`() = runTest {
        // Given
        val testData = TestData.createUser()

        // When
        viewModel.onEvent(Event.Load)
        advanceUntilIdle()

        // Then
        assertState { it.isLoaded }

        // And verify effect
        viewModel.effect.test {
            assertEquals(Effect.Success, awaitItem())
        }
    }
}
```

## Additional Resources

- [Kotlin Test Documentation](https://kotlinlang.org/api/latest/kotlin.test/)
- [Kotlinx Coroutines Test](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-test/)
- [Turbine Documentation](https://github.com/cashapp/turbine)
- [Ktor Client Mock](https://ktor.io/docs/client-testing.html)
- [Koin Testing](https://insert-koin.io/docs/reference/koin-test/testing)

## Contributing

When adding new features, always include tests:

1. **Unit tests** for business logic (use cases)
2. **ViewModel tests** for UI logic
3. **Integration tests** for repositories
4. **End-to-end tests** for critical user flows

Aim for >80% code coverage for new code.
