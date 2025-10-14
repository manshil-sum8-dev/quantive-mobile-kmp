package za.co.quantive.app.domain.usecase

import kotlinx.coroutines.test.runTest
import za.co.quantive.app.testing.BaseTest
import za.co.quantive.app.testing.TestData
import za.co.quantive.app.testing.mock.MockAuthRepository
import za.co.quantive.shared.utils.Result
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Example test for LoginUseCase
 * Demonstrates testing use cases with mock repositories
 */
class LoginUseCaseTest : BaseTest() {

    private lateinit var mockRepository: MockAuthRepository
    private lateinit var useCase: LoginUseCase

    @BeforeTest
    override fun setUp() {
        super.setUp()
        mockRepository = MockAuthRepository()
        useCase = LoginUseCase(mockRepository)
    }

    @AfterTest
    override fun tearDown() {
        super.tearDown()
        mockRepository.reset()
    }

    @Test
    fun `test login with valid credentials returns success`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val params = LoginUseCase.Params(email, password)

        // When
        val result = useCase(params)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(email, (result as Result.Success).data.user.email)
    }

    @Test
    fun `test login with empty email returns error`() = runTest {
        // Given
        val params = LoginUseCase.Params(
            email = "",
            password = "password123"
        )

        // When
        val result = useCase(params)

        // Then
        assertTrue(result is Result.Error)
        assertEquals("Please enter your email address", (result as Result.Error).message)
    }

    @Test
    fun `test login with empty password returns error`() = runTest {
        // Given
        val params = LoginUseCase.Params(
            email = "test@example.com",
            password = ""
        )

        // When
        val result = useCase(params)

        // Then
        assertTrue(result is Result.Error)
        assertEquals("Please enter your password", (result as Result.Error).message)
    }

    @Test
    fun `test login with repository error returns error`() = runTest {
        // Given
        mockRepository.shouldReturnError = true
        mockRepository.errorMessage = "Network error"
        val params = LoginUseCase.Params(
            email = "test@example.com",
            password = "password123"
        )

        // When
        val result = useCase(params)

        // Then
        assertTrue(result is Result.Error)
        assertEquals("Network error", (result as Result.Error).message)
    }
}
