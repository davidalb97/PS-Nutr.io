package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.ingredient

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal.MealItemInput

data class IngredientContainerInput(
    val ingredients: Iterable<MealItemInput>
)