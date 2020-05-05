package pt.isel.ps.g06.httpserver.dataAccess.api

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import java.net.http.HttpResponse

abstract class ApiResponseMapper(protected val jsonMapper: ObjectMapper) {
    fun <T> mapTo(response: HttpResponse<String>, klass: Class<T>): T {
        val body = response.body()

        try {
            return jsonMapper.readValue(body, klass)
        } catch (ex: JsonMappingException) {
            throw mapResponseToException(body)
        }
    }

    protected abstract fun mapResponseToException(responseBody: String): Exception
}