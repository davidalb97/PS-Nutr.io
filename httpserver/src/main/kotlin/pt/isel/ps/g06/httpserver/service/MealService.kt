package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.DbMealResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.IngredientInput
import pt.isel.ps.g06.httpserver.model.Meal

@Service
class MealService(
        private val dbMealRepo: MealDbRepository,
//        private val dbRestaurantMealRepo: RestaurantMealDbRepository,
//        private val dbRestaurantMealResponseMapper: DbRestaurantMealResponseMapper,
        private val dbMealResponseMapper: DbMealResponseMapper
) {
    fun getMeal(mealId: Int, userId: Int): Meal? {
        return dbMealRepo
                .getById(mealId)
                ?.let(dbMealResponseMapper::mapTo)
    }

    fun createMeal(
            submitterId: Int,
            name: String,
            quantity: Int,
            ingredients: Collection<IngredientInput>,
            cuisines: Collection<String>
    ): Meal {
        val createdMeal = dbMealRepo.insert(
                submitterId = submitterId,
                mealName = name,
                cuisines = cuisines,
                ingredients = ingredients,
                quantity = quantity
        )

        return dbMealResponseMapper.mapTo(createdMeal)
    }
}