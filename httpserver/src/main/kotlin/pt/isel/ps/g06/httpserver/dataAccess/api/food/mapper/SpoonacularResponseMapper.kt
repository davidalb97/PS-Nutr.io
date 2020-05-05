package pt.isel.ps.g06.httpserver.dataAccess.api.food.mapper

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.common.ApiResponseMapper

@Component
class SpoonacularResponseMapper(jsonMapper: ObjectMapper) : ApiResponseMapper(jsonMapper) {
    override fun mapResponseToException(responseBody: String): Exception {
        TODO("Not yet implemented")
    }
}