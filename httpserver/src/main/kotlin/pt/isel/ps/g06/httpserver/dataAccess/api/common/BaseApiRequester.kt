package pt.isel.ps.g06.httpserver.dataAccess.api.common

import com.fasterxml.jackson.databind.ObjectMapper
import java.net.http.HttpClient

open class BaseApiRequester(
        protected val httpClient: HttpClient = HttpClient.newHttpClient(),
        protected val responseMapper: ObjectMapper
)