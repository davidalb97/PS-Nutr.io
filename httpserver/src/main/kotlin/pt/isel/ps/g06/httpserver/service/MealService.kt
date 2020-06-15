package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.clientError.InvalidMealException
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
    fun getMeal(mealId: Int): Meal? {
        return dbMealRepo
                .getById(mealId)
                ?.let(dbMealResponseMapper::mapTo)
    }

    fun getSuggestedMeals(): Sequence<Meal> {
        return dbMealRepo
                .getAllSuggestedMeals()
                .map { dbMealResponseMapper.mapTo(it) }
    }

    fun createMeal(
            submitterId: Int,
            name: String,
            quantity: Int,
            ingredients: Collection<IngredientInput>,
            cuisines: Collection<String>
    ): Meal {
        if (ingredients.sumBy { it.quantity!! } > quantity) {
            throw InvalidMealException("The sum of ingredient quantity must be lower than meal quantity!")
        }

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