package pt.ipl.isel.leic.ps.androidclient.data.api.dto

import pt.ipl.isel.leic.ps.androidclient.data.model.Ingredient
import java.util.stream.Stream

class ApiMealInfoDto(
    val carbohydrates: Float?,
    val ingredients: Stream<Ingredient>
)
