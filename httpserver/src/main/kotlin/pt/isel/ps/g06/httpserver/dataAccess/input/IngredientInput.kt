package pt.isel.ps.g06.httpserver.dataAccess.input

import javax.validation.constraints.NotBlank

data class IngredientInput(
        @field:NotBlank(message = "Ingredient name must not be empty!")
        val name: String?,
        @field:NotBlank(message = "Ingredient identifier must not be empty!")
        val identifier: String?
)