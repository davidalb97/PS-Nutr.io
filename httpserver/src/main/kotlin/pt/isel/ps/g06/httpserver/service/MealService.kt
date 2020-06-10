package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food.MealResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.IngredientInput
import pt.isel.ps.g06.httpserver.model.Meal

@Service
class MealService(
        private val mealResponseMapper: MealResponseMapper,
        private val mealDbRepository: MealDbRepository
) {

    fun getMeal(mealId: Int): Meal? {
        return mealDbRepository.getById(mealId)?.let(mealResponseMapper::mapTo)
    }

    fun createMeal(
            submitterId: Int,
            name: String,
            quantity: Int,
            ingredients: Collection<IngredientInput>,
            cuisines: Collection<String>
    ): Meal {
        val createdMeal = mealDbRepository.insert(
                submitterId = submitterId,
                mealName = name,
                cuisines = cuisines,
                ingredients = ingredients,
                quantity = quantity
        )

        return mealResponseMapper.mapTo(createdMeal)
    }

    fun getAllMealsFromRestaurant(restaurantId: Int): Collection<Meal>? {
        return mealDbRepository
                .getAllMealsFromRestaurant(restaurantId)
                .map { mealResponseMapper.mapTo(it) }
    }
}