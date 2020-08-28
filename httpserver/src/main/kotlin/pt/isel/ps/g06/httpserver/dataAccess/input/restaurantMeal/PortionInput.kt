package pt.isel.ps.g06.httpserver.dataAccess.input.restaurantMeal

import javax.validation.constraints.Min
import javax.validation.constraints.Pattern

data class PortionInput(
        @field:Min(1, message = "You must provide a positive portion quantity!")
        val quantity: Int?,
        @field:Pattern(regexp = "(gr)", message = "You must provide a valid unit! Allowed ones are: 'gr'")
        val unit: String?
)