package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import pt.isel.ps.g06.httpserver.common.exception.RestaurantNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.api.common.ApiResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.ZomatoErrorDto

@Component
class ZomatoResponseMapper(jsonMapper: ObjectMapper) : ApiResponseMapper(jsonMapper) {
    override fun mapResponseToException(responseBody: String): Exception {
        val error = jsonMapper.readValue(responseBody, ZomatoErrorDto::class.java)

        return when (error.code) {
            HttpStatus.NOT_FOUND.value() -> RestaurantNotFoundException()
            else -> ResponseStatusException(HttpStatus.BAD_GATEWAY)
        }
    }
}