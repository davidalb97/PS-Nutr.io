package pt.isel.ps.g06.httpserver.dataAccess.model

import pt.isel.ps.g06.httpserver.model.Meal
import java.net.URI

class SimplifiedMealOutputModel(
        val identifier: String,
        val name: String,
        val image: URI?
)

fun toSimplifiedMeal(meal: Meal): SimplifiedMealOutputModel {
    return SimplifiedMealOutputModel(
            identifier = meal.identifier,
            name = meal.name,
            image = meal.image
    )
}