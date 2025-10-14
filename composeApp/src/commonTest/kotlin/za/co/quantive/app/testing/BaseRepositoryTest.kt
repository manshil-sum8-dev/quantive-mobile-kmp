package za.co.quantive.app.testing

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.Json

/**
 * Base test class for testing repositories
 * Provides utilities for mocking HTTP responses
 */
abstract class BaseRepositoryTest : BaseTest() {

    /**
     * Creates a mock HttpClient for testing
     */
    protected fun createMockHttpClient(
        responseHandler: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData
    ): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler(responseHandler)
            }
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    /**
     * Creates a successful mock response within MockRequestHandleScope
     */
    protected fun MockRequestHandleScope.mockSuccessResponse(
        content: String,
        status: HttpStatusCode = HttpStatusCode.OK
    ): HttpResponseData {
        return respond(
            content = content,
            status = status,
            headers = headersOf(
                HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())
            )
        )
    }

    /**
     * Creates an error mock response within MockRequestHandleScope
     */
    protected fun MockRequestHandleScope.mockErrorResponse(
        content: String = """{"error": "Something went wrong"}""",
        status: HttpStatusCode = HttpStatusCode.InternalServerError
    ): HttpResponseData {
        return respond(
            content = content,
            status = status,
            headers = headersOf(
                HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString())
            )
        )
    }
}
