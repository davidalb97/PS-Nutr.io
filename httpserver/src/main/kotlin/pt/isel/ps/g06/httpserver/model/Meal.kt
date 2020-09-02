package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.db.MealType
import pt.isel.ps.g06.httpserver.model.modular.BasePublicSubmission
import pt.isel.ps.g06.httpserver.model.modular.ICuisines
import pt.isel.ps.g06.httpserver.model.modular.INutritionalSubmission
import pt.isel.ps.g06.httpserver.model.modular.UserPredicate
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import pt.isel.ps.g06.httpserver.model.restaurant.RestaurantIdentifier
import java.net.URI
import java.time.OffsetDateTime
import java.util.stream.Stream
import kotlin.streams.toList

class Meal(
        identifier: Int,
        name: String,
        isFavorite: UserPredicate,
        image: URI?,
        override val nutritionalInfo: NutritionalValues,
        val composedBy: MealComposition,
        override val cuisines: Stream<Cuisine>,
        val submitterInfo: Lazy<Submitter?>,
        val creationDate: Lazy<OffsetDateTime?>,
        val type: MealType,
        private val restaurantInfoSupplier: (RestaurantIdentifier) -> MealRestaurantInfo?
) : BasePublicSubmission<Int>(
        identifier = identifier,
        name = name,
        image = image,
        isFavorable = { submitterInfo.value?.identifier != it ?: true },
        isFavorite = isFavorite
), INutritionalSubmission, ICuisines {

    private val restaurantInfo: MutableMap<RestaurantIdentifier, MealRestaurantInfo?> = mutableMapOf()

    /**
     * Checks if given Meal belongs to a User.
     * This also allows to know that given Meal **is not** a suggested Meal, if false, as suggested Meals
     * have no submitter/owner
     *
     */
    fun isUserMeal(): Boolean = type == MealType.CUSTOM

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

        return restaurantCuisines.any { restaurantCuisine ->
            mealCuisines.any { mealCuisine ->
                restaurantCuisine.name == mealCuisine.name
            }
        }
    }
}