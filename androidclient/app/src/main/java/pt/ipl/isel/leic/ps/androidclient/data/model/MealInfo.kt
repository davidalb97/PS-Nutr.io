package pt.ipl.isel.leic.ps.androidclient.data.model

import java.util.stream.Stream


data class MealInfo (
    val carbohydrates: Float?,
    val ingredients: Stream<Ingredient>
)
