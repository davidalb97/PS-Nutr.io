package pt.isel.ps.g06.httpserver.dataAccess.input.meal

import pt.isel.ps.g06.httpserver.dataAccess.input.ingredient.IngredientInput
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

data class CustomMealInput(
        @field:NotBlank(message = "A name must be given for the meal!")
        val name: String?,
        @field:Min(value = 1, message = "A meal quantity must be given!")
        val quantity: Float?,
        @field:Pattern(regexp = "(gr)", message = "You must provide a valid unit! Allowed ones are: 'gr'")
        val unit: String?,
        @field:NotEmpty(message = "You must give at least one ingredient!")
        @field:Valid
        val ingredients: Collection<IngredientInput>?,
        @field:NotEmpty(message = "You must give at least one cuisine!")
        val cuisines: Collection<String>?
)