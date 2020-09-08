package pt.isel.ps.g06.httpserver.dataAccess.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Repository
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest

@Repository
class BaseApiRequester(
        protected val httpClient: HttpClient = HttpClient.newHttpClient(),
        protected val objectMapper: ObjectMapper
) {
    protected fun buildGetRequest(uri: URI): HttpRequest {
        return HttpRequest
                .newBuilder()
                .GET()
                .uri(uri)
                .build()
    }
}