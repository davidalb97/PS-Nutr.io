package pt.isel.ps.g06.httpserver.dataAccess.output

import java.net.URI
import java.time.OffsetDateTime

class DetailedRestaurantMealOutput(
        id: Int,
        name: String,
        image: URI?,
        votes: VotesOutput?,
        isFavorite: Boolean,
        val creationDate: OffsetDateTime,
        val composedBy: MealComposition?,
        val nutritionalInfo: NutritionalInfoOutput,
        val portions: Collection<Int>,
        val createdBy: SimplifiedUserOutput?
) : SimplifiedRestaurantMealOutput(
        mealIdentifier = id,
        name = name,
        votes = votes,
        isFavorite = isFavorite,
        imageUri = image
)