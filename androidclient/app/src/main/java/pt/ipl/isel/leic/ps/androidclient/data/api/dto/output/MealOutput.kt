package pt.ipl.isel.leic.ps.androidclient.data.api.dto.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedIngredientInput

data class MealOutput(
    val name: String,
    val quantity: Int,
    val ingredients: Iterable<SimplifiedIngredientInput>,
    val cuisines: Collection<String>?
)