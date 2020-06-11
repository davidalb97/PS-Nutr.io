package pt.isel.ps.g06.httpserver.dataAccess.output

import java.time.OffsetDateTime

data class DetailedMealOutputDto(
        val mealItem: ItemMealOutputDto,
        val user: UserOutputDto,
        val ingredient: Collection<MealIngredientOutputDto>,
        val cuisines: Collection<String>,
        val creationDate: OffsetDateTime
)