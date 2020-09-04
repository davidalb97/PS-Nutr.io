package pt.isel.ps.g06.httpserver.dataAccess.output.restaurantMeal

import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.RestaurantMeal
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant

data class RestaurantMealsFromRestaurantContainerOutput(
        val restaurantIdentifier: String,
        val suggestedMeals: Collection<SimplifiedRestaurantMealOutput>,
        val userMeals: Collection<SimplifiedRestaurantMealOutput>
)

fun toRestaurantMealsFromRestaurantContainerOutput(restaurant: Restaurant, userId: Int? = null): RestaurantMealsFromRestaurantContainerOutput {
    val mealToRestaurantMeal: (Meal) -> SimplifiedRestaurantMealOutput = { meal: Meal ->
        toSimplifiedRestaurantMealOutput(
                restaurantMeal = RestaurantMeal(
                        restaurant = restaurant,
                        meal = meal,
                        info = meal.getMealRestaurantInfo(restaurant.identifier.value)
                ),
                userId = userId
        )
    }
    return RestaurantMealsFromRestaurantContainerOutput(
            restaurantIdentifier = restaurant.identifier.value.toString(),
            suggestedMeals = restaurant.suggestedMeals
                    .map { mealToRestaurantMeal(it) }
                    .toList(),
            userMeals = restaurant.meals
                    .map { mealToRestaurantMeal(it) }
                    .toList()
    )
}