package za.co.quantive.app.data.repository

import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import za.co.quantive.app.data.datasource.AuthRemoteDataSourceImpl
import za.co.quantive.app.testing.BaseRepositoryTest
import za.co.quantive.app.testing.TestData
import za.co.quantive.shared.models.LoginRequest
import za.co.quantive.shared.network.ApiConfig
import za.co.quantive.shared.network.api.AuthApiClient
import za.co.quantive.shared.utils.Result
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Example test for AuthRepositoryImpl
 * Demonstrates testing repositories with mock HTTP clients
 */
class AuthRepositoryImplTest : BaseRepositoryTest() {

    private lateinit var repository: AuthRepositoryImpl

    @BeforeTest
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun `test login success returns auth response`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "password123"
        val expectedResponse = TestData.createAuthResponse()

        val mockClient = createMockHttpClient {
            // Return success response
            mockSuccessResponse(
                content = Json.encodeToString(expectedResponse),
                status = HttpStatusCode.OK
            )
        }

        val apiClient = AuthApiClient(
            httpClient = mockClient,
            config = ApiConfig(
                baseUrl = "http://localhost:8080",
                enableLogging = false
            )
        )

        val dataSource = AuthRemoteDataSourceImpl(apiClient)
        repository = AuthRepositoryImpl(dataSource)

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(expectedResponse.user.email, (result as Result.Success).data.user.email)
    }

    @Test
    fun `test login failure returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "wrongpassword"

        val mockClient = createMockHttpClient {
            // Return error response
            mockErrorResponse(
                content = """{"error": "Invalid credentials"}""",
                status = HttpStatusCode.Unauthorized
            )
        }

        val apiClient = AuthApiClient(
            httpClient = mockClient,
            config = ApiConfig(
                baseUrl = "http://localhost:8080",
                enableLogging = false
            )
        )

        val dataSource = AuthRemoteDataSourceImpl(apiClient)
        repository = AuthRepositoryImpl(dataSource)

        // When
        val result = repository.login(email, password)

        // Then
        assertTrue(result is Result.Error)
    }

    @Test
    fun `test logout clears user data`() = runTest {
        // Given
        val mockClient = createMockHttpClient {
            mockSuccessResponse(
                content = "{}",
                status = HttpStatusCode.OK
            )
        }

        val apiClient = AuthApiClient(
            httpClient = mockClient,
            config = ApiConfig(
                baseUrl = "http://localhost:8080",
                enableLogging = false
            )
        )

        val dataSource = AuthRemoteDataSourceImpl(apiClient)
        repository = AuthRepositoryImpl(dataSource)

        // When
        val result = repository.logout()

        // Then
        assertTrue(result is Result.Success)

        // Verify user is cleared
        val currentUser = repository.getCurrentUser()
        assertTrue(currentUser is Result.Success)
        assertEquals(null, (currentUser as Result.Success).data)
    }
}
