package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.MEAL
import pt.isel.ps.g06.httpserver.common.exception.clientError.InvalidMealException
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.DbMealResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.FavoriteDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.IngredientInput
import pt.isel.ps.g06.httpserver.model.Meal

@Service
class MealService(
        private val dbMealRepository: MealDbRepository,
        private val dbFavoriteRepository: FavoriteDbRepository,
        private val dbMealResponseMapper: DbMealResponseMapper
) {
    fun setFavorite(mealId: Int, userId: Int, isFavorite: Boolean): Boolean {
        return dbFavoriteRepository.setFavorite(mealId, userId, isFavorite)
    }

    fun getMeal(mealId: Int): Meal? {
        return dbMealRepository
                .getById(mealId)
                ?.let(dbMealResponseMapper::mapTo)
    }

    fun getSuggestedMeals(count: Int?, skip: Int?, cuisines: Collection<String>?): Sequence<Meal> {
        return dbMealRepository
                .getAllSuggestedMeals(count, skip, cuisines)
                .map { dbMealResponseMapper.mapTo(it) }
    }

    fun getUserCustomMeals(submitterId: Int, count: Int?, skip: Int?): Sequence<Meal> =
            dbMealRepository
                    .getBySubmitterId(submitterId, count, skip)
                    .map(dbMealResponseMapper::mapTo)

    fun createCustomMeal(
            submitterId: Int,
            name: String,
            quantity: Int,
            ingredients: Collection<IngredientInput>,
            cuisines: Collection<String>
    ): Meal {
        validateMealQuantity(ingredients, quantity)

        val createdMeal = dbMealRepository.insertCustom(
                submitterId = submitterId,
                submissionType = SubmissionType.MEAL,
                mealName = name,
                cuisines = cuisines,
                ingredients = ingredients,
                quantity = quantity
        )

        return dbMealResponseMapper.mapTo(createdMeal)
    }

    fun createSuggestedMeal(
            submitterId: Int,
            name: String,
            quantity: Int,
            ingredients: Collection<IngredientInput>,
            cuisines: Collection<String>
    ): Meal {

        validateMealQuantity(ingredients, quantity)

        val createdMeal = dbMealRepository.insert(
                submitterId = submitterId,
                submissionType = SubmissionType.MEAL,
                mealName = name,
                cuisines = cuisines,
                ingredients = ingredients,
                quantity = quantity
        )

        return dbMealResponseMapper.mapTo(createdMeal)
    }

    private fun validateMealQuantity(ingredients: Collection<IngredientInput>, quantity: Int) {
        if (ingredients.sumBy { it.quantity!! } > quantity) {
            throw InvalidMealException("The sum of ingredient quantity must be lower than meal quantity!")
        }
    }
}