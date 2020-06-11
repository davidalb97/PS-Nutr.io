package pt.isel.ps.g06.httpserver.dataAccess.output

import java.time.OffsetDateTime

data class DetailedRestaurantMealOutputDto(
        val meal: ItemRestaurantMealOutputDto,
        val portions: Collection<Int>,
        val user: UserOutputDto
)