package pt.isel.ps.g06.httpserver.dataAccess.input.restaurantMeal

import javax.validation.constraints.Min

data class RestaurantMealInput(
        @field:Min(value = 1, message = "A positive meal identifier must be given!")
        val mealId: Int?
)