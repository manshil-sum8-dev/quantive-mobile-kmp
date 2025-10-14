package za.co.quantive.shared.network

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import za.co.quantive.shared.utils.Result

/**
 * Base API client with common HTTP operations and error handling
 */
abstract class BaseApiClient(
    protected val httpClient: HttpClient,
    protected val config: ApiConfig
) {

    /**
     * Performs a GET request and returns a Result
     *
     * @param endpoint The API endpoint path
     * @param parameters Optional query parameters
     * @return Result with the response data or error
     */
    protected suspend inline fun <reified T> get(
        endpoint: String,
        parameters: Map<String, Any?>? = null
    ): Result<T> {
        return safeApiCall {
            httpClient.get(endpoint) {
                parameters?.forEach { (key, value) ->
                    parameter(key, value)
                }
            }.body()
        }
    }

    /**
     * Performs a POST request and returns a Result
     *
     * @param endpoint The API endpoint path
     * @param body Request body to send
     * @return Result with the response data or error
     */
    protected suspend inline fun <reified T, reified B> post(
        endpoint: String,
        body: B
    ): Result<T> {
        return safeApiCall {
            httpClient.post(endpoint) {
                setBody(body)
            }.body()
        }
    }

    /**
     * Performs a PUT request and returns a Result
     *
     * @param endpoint The API endpoint path
     * @param body Request body to send
     * @return Result with the response data or error
     */
    protected suspend inline fun <reified T, reified B> put(
        endpoint: String,
        body: B
    ): Result<T> {
        return safeApiCall {
            httpClient.put(endpoint) {
                setBody(body)
            }.body()
        }
    }

    /**
     * Performs a PATCH request and returns a Result
     *
     * @param endpoint The API endpoint path
     * @param body Request body to send
     * @return Result with the response data or error
     */
    protected suspend inline fun <reified T, reified B> patch(
        endpoint: String,
        body: B
    ): Result<T> {
        return safeApiCall {
            httpClient.patch(endpoint) {
                setBody(body)
            }.body()
        }
    }

    /**
     * Performs a DELETE request and returns a Result
     *
     * @param endpoint The API endpoint path
     * @return Result with the response data or error
     */
    protected suspend inline fun <reified T> delete(
        endpoint: String
    ): Result<T> {
        return safeApiCall {
            httpClient.delete(endpoint).body()
        }
    }

    /**
     * Wraps API calls with error handling
     *
     * @param apiCall The API call to execute
     * @return Result with success data or error
     */
    protected suspend inline fun <T> safeApiCall(
        crossinline apiCall: suspend () -> T
    ): Result<T> {
        return try {
            Result.Success(apiCall())
        } catch (e: Exception) {
            Result.Error(
                exception = e,
                message = parseErrorMessage(e)
            )
        }
    }

    /**
     * Parses error messages from exceptions
     *
     * @param exception The exception to parse
     * @return Human-readable error message
     */
    protected fun parseErrorMessage(exception: Exception): String {
        return when (exception) {
            is io.ktor.client.plugins.ClientRequestException -> {
                "Client error: ${exception.response.status.description}"
            }
            is io.ktor.client.plugins.ServerResponseException -> {
                "Server error: ${exception.response.status.description}"
            }
            is io.ktor.client.plugins.HttpRequestTimeoutException -> {
                "Request timed out. Please check your connection."
            }
            else -> {
                exception.message ?: "An unexpected error occurred"
            }
        }
    }

    /**
     * Closes the HTTP client
     */
    fun close() {
        httpClient.close()
    }
}
