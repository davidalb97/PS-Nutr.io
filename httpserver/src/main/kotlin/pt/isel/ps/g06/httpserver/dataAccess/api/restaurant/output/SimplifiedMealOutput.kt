package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.output

import pt.isel.ps.g06.httpserver.model.Meal
import java.net.URI

data class SimplifiedMealOutput(val id: String, val name: String, val image: URI?)

fun toSimplifiedMealOutput(meal: Meal): SimplifiedMealOutput = SimplifiedMealOutput(
        id = meal.identifier,
        name = meal.name,
        image = meal.image
)