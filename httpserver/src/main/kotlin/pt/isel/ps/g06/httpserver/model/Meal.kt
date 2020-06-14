package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.model.Cuisine
import pt.isel.ps.g06.httpserver.dataAccess.model.MealComposition
import java.net.URI
import java.time.OffsetDateTime

data class Meal(
        val identifier: Int,
        val name: String,
        val isFavorite: (Int) -> Boolean,
        val imageUri: URI?,
        val nutritionalValues: NutritionalValues,
        val composedBy: MealComposition,
        val cuisines: Sequence<Cuisine>,
        val creatorInfo: Lazy<Creator?>,
        val creationDate: Lazy<OffsetDateTime?>,
        val restaurantInfo: (RestaurantIdentifier) -> MealRestaurantInfo?
) {
    fun isUserMeal(): Boolean {
        return creatorInfo.value != null
    }
}