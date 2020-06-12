package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

import java.net.URI
import java.time.OffsetDateTime

class DetailedRestaurantMealInputDto(
    id: Int,
    name: String,
    image: URI?,
    votes: VotesInputDto?,
    isFavorite: Boolean,
    val creationDate: OffsetDateTime,
    val composedBy: _MealComposition?,
    val nutritionalInfo: _NutritionalInfoInput?,
    val portions: Collection<Int>,
    val createdBy: SimplifiedUserInputDto?
): SimplifiedRestaurantMealInputDto(
    id = id,
    name = name,
    votes = votes,
    isFavorite = isFavorite,
    image = image
)