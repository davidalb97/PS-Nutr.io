package pt.isel.ps.g06.httpserver.dataAccess.input.ingredient

import javax.validation.constraints.NotNull

data class IngredientInput(
        @field:NotNull(message = "Ingredient identifier must not be empty!")
        val identifier: Int?,
        @field:NotNull(message = "An ingredient quantity must be given!")
        val quantity: Float?
)