package pt.isel.ps.g06.httpserver.dataAccess.api

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class HttpApiClient(private val httpClient: HttpClient) {
    fun <T> request(
            uri: String,
            headers: Map<String, String>? = null,
            exceptionPredicate: (HttpResponse<String>) -> Boolean,
            bodyMapper: (HttpResponse<String>) -> T
    ): T {

        val httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .also { requestHeaders -> headers?.forEach { requestHeaders.header(it.key, it.value) } }
                .build()

        val response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())

        if (exceptionPredicate.invoke(response)) throw Exception()
        else return bodyMapper.invoke(response)
    }
}