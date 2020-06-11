package pt.isel.ps.g06.httpserver.dataAccess.output

import java.time.OffsetDateTime

data class DetailedMealOutputDto(
        val id: Int,
        val name: String,
        val carbs: Int,
        val amount: Int,
        val unit: String,
        val image: String,
        val user: UserOutputDto,
        val ingredient: Collection<MealIngredientOutputDto>,
        val creationDate: OffsetDateTime
)