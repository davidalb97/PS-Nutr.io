package pt.isel.ps.g06.httpserver.dataAccess.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.json.JacksonJsonParser
import java.net.http.HttpClient

abstract class BaseApiRequester(val httpClient: HttpClient, val jsonMapper: ObjectMapper) {
}