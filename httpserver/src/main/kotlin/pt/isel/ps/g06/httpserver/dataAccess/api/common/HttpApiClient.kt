package pt.isel.ps.g06.httpserver.dataAccess.api.common

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class HttpApiClient(private val httpClient: HttpClient) {
    fun <T> request(
            uri: URI,
            headers: Map<String, String> = emptyMap(),
            bodyMapper: (HttpResponse<String>) -> T
    ): T {

        val httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .also { request -> headers.forEach { request.header(it.key, it.value) } }
                .build()

        val response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
        return bodyMapper.invoke(response)
    }
}