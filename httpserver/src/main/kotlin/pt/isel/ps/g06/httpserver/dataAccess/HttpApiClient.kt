package pt.isel.ps.g06.httpserver.dataAccess

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

class HttpApiClient(private val httpClient: HttpClient) {
    fun <T> request(
            uri: String,
            headers: Map<String, String>? = null,
            exceptionPredicate: (HttpResponse<String>) -> Boolean,
            bodyMapper: (HttpResponse<String>) -> T
    ): CompletableFuture<T> {

        val httpRequest = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(uri))
                .also { requestHeaders -> headers?.forEach { requestHeaders.header(it.key, it.value) } }
                .build()

        return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply {
                    if (exceptionPredicate.invoke(it)) throw Exception()
                    else return@thenApply bodyMapper.invoke(it)
                }
    }
}