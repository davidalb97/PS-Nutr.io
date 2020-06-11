package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food.RestaurantMealItemResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.MealInfoResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.IngredientInput
import pt.isel.ps.g06.httpserver.model.MealInfo
import pt.isel.ps.g06.httpserver.model.RestaurantMealItem

@Service
class MealService(
        private val mealDbRepository: MealDbRepository,
        private val restaurantMealDbRepository: RestaurantMealDbRepository,
        private val restaurantMealItemResponseMapper: RestaurantMealItemResponseMapper,
        private val mealInfoResponseMapper: MealInfoResponseMapper
) {

    fun getAllMealsFromRestaurant(restaurantId: Int, userId: Int): Collection<RestaurantMealItem> {
        return restaurantMealDbRepository
                .getRestaurantMeals(restaurantId, userId)
                .map(restaurantMealItemResponseMapper::mapTo)
    }

    fun getMeal(mealId: Int, userId: Int): MealInfo? {
        return mealDbRepository
                .getById(mealId, userId)
                ?.let(mealInfoResponseMapper::mapTo)
    }

    fun createMeal(
            submitterId: Int,
            name: String,
            quantity: Int,
            ingredients: Collection<IngredientInput>,
            cuisines: Collection<String>
    ): MealInfo {
        val createdMeal = mealDbRepository.insert(
                submitterId = submitterId,
                mealName = name,
                cuisines = cuisines,
                ingredients = ingredients,
                quantity = quantity
        )

        //TODO mapTo Meal
        return MealInfo(
                1,
                "Placeholder",
                false,
                "",
                100,
                100,
                "g",
                emptyList(),
                emptyList()
        )
    }
}