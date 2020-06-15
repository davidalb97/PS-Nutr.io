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
        //TODO See usages and replace by restaurantInfo map function call
        val restaurantInfoSupplier: (RestaurantIdentifier) -> MealRestaurantInfo?,
        val restaurantInfo: MutableMap<RestaurantIdentifier, MealRestaurantInfo?> = mutableMapOf()
) {
    /**
     * Checks if given Meal belongs to a User.
     * This also allows to know that given Meal **is not** a suggested Meal, if false, as suggested Meals
     * have no submitter/owner
     *
     * *This operation initializes [creatorInfo] value.*
     */
    fun isUserMeal(): Boolean {
        return creatorInfo.value != null
    }

    fun isRestaurantMeal(restaurant: Restaurant): Boolean {
        return if (isUserMeal()) {
            getMealRestaurantInfo(restaurant.identifier.value)
                    ?.let { true }
                    ?: false
        } else {
            isSuggestedMeal(restaurant)
        }
    }


    fun getMealRestaurantInfo(restaurantIdentifier: RestaurantIdentifier): MealRestaurantInfo? {
        return restaurantInfo.getOrPut(restaurantIdentifier) { restaurantInfoSupplier(restaurantIdentifier) }
    }

    private fun isSuggestedMeal(restaurant: Restaurant): Boolean {
        val restaurantCuisines = restaurant.cuisines.toList()
        val mealCuisines = cuisines.toList()

        return restaurantCuisines
                .intersect(mealCuisines)
                .isNotEmpty()
    }
}