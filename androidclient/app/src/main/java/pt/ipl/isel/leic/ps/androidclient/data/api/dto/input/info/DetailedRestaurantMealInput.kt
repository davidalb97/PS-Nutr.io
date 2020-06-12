package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.*
import java.net.URI
import java.time.OffsetDateTime

class DetailedRestaurantMealInput(
    id: Int,
    name: String,
    image: URI?,
    votes: VotesInputDto?,
    isFavorite: Boolean,
    val creationDate: OffsetDateTime,
    val composedBy: MealComposition?,
    val nutritionalInfo: NutritionalInfoInput?,
    val portions: Collection<Int>,
    val createdBy: SimplifiedUserInput?
): SimplifiedRestaurantMealInput(
    id = id,
    name = name,
    votes = votes,
    isFavorite = isFavorite,
    image = image
)