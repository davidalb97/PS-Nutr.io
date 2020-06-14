package pt.ipl.isel.leic.ps.androidclient.data.api.dto.output

import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient

data class MealOutput(
    val name: String,
    val quantity: Int,
    val ingredients: Iterable<MealIngredient>,
    val cuisines: Collection<String>?
)