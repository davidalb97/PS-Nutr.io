package pt.isel.ps.g06.httpserver.dataAccess.api

import org.springframework.boot.json.JacksonJsonParser
import java.net.HttpURLConnection
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest

//TODO This shouldn't be here, for testing only
private const val ZOMATO_BASE_URL = "https://developers.zomato.com/api/v2.1/"
private const val ZOMATO_API_KEY = "3e128506ffbfc1c23b4e2b6acd3eb84b"



class ZomatoApi(httpClient: HttpClient, jackson: JacksonJsonParser) : BaseApiRequester(httpClient, jackson) {
    fun a() {
        HttpRequest.newBuilder()
                .GET()
                .uri("${ZOMATO_BASE_URL}")


        httpClient.send()
    }
}
