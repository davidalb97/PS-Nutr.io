package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedMealInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.SimplifiedUserInput
import java.net.URI
import java.time.OffsetDateTime

class DetailedMealInput(
    id: Int,
    name: String,
    image: URI?,
    isFavorite: Boolean,
    val creationDate: OffsetDateTime,
    val composedBy: MealComposition?,
    val nutritionalInfo: NutritionalInfoInput?,
    val createdBy: SimplifiedUserInput?
) : SimplifiedMealInput(
    id = id,
    name = name,
    image = image,
    isFavorite = isFavorite
)