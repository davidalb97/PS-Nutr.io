package pt.isel.ps.g06.httpserver.dataAccess.output

import java.time.OffsetDateTime

data class DetailedRestaurantOutputDto(
        val restaurantItem: ItemRestaurantOutputDto,
        val cuisines: Collection<String>,
        val creationDate: OffsetDateTime,
        val meals: Collection<ItemRestaurantMealOutputDto>,
        val suggestedMeals: Collection<ItemMealOutputDto>
)