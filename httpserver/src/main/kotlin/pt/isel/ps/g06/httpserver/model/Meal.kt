package pt.isel.ps.g06.httpserver.model

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
        val submitterInfo: Lazy<Submitter?>,
        val creationDate: Lazy<OffsetDateTime?>,
        private val restaurantInfoSupplier: (RestaurantIdentifier) -> MealRestaurantInfo?,
        private val restaurantInfo: MutableMap<RestaurantIdentifier, MealRestaurantInfo?> = mutableMapOf()
) {
    /**
     * Checks if given Meal belongs to a User.
     * This also allows to know that given Meal **is not** a suggested Meal, if false, as suggested Meals
     * have no submitter/owner
     *
     * *This operation initializes [submitterInfo] value.*
     */
    fun isUserMeal(): Boolean {
        return submitterInfo.value != null
    }

    fun isMealOwner(user: User?): Boolean =
            user != null && isUserMeal() && user.identifier != submitterInfo.value?.identifier

    fun isRestaurantMeal(restaurant: Restaurant): Boolean {
        return getMealRestaurantInfo(restaurant.identifier.value)
                ?.let { true }
                ?: isSuggestedMeal(restaurant)
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