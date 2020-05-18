package pt.isel.ps.g06.httpserver.dataAccess.api.common

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest

open class BaseApiRequester(
        protected val httpClient: HttpClient = HttpClient.newHttpClient(),
        protected val responseMapper: ObjectMapper
) {
    protected open fun buildGetRequest(uri: URI): HttpRequest {
        return HttpRequest
                .newBuilder()
                .GET()
                .uri(uri)
                .build()
    }
}