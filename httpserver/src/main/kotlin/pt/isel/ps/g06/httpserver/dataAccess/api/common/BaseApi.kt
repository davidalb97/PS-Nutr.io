package pt.isel.ps.g06.httpserver.dataAccess.api.common

import java.net.URI

open class BaseApi(private val httpClient: HttpApiClient, private val responseMapper: ApiResponseMapper) {
    protected fun <D> requestDto(uri: URI, mapTo: Class<D>, headers: Map<String, String> = emptyMap()): D {
        return httpClient.request(uri, headers) { responseMapper.mapTo(it, mapTo) }
    }
}