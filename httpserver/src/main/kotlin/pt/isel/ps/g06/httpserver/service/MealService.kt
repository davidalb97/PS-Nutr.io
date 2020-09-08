package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.db.MealType
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.FavoriteDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.ingredient.IngredientInput
import pt.isel.ps.g06.httpserver.model.mapper.restaurant.DbMealResponseMapper
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.InvalidMealException
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

    fun getSuggestedMeals(skip: Int?, count: Int?, cuisines: Collection<String>?): Sequence<Meal> {
        return dbMealRepository
                .getAllSuggestedMeals(skip, count, cuisines)
                .map { dbMealResponseMapper.mapTo(it) }
    }

    fun getUserCustomMeals(submitterId: Int, skip: Int?, count: Int?): Sequence<Meal> =
            dbMealRepository
                    .getBySubmitterId(submitterId, skip, count)
                    .map(dbMealResponseMapper::mapTo)

    fun getUserFavoriteMeals(submitterId: Int, count: Int?, skip: Int?): Sequence<Meal> =
        dbMealRepository
                .getAllUserFavorites(submitterId, count, skip)
                .map(dbMealResponseMapper::mapTo)

    fun createMeal(
            submitterId: Int,
            name: String,
            quantity: Int,
            ingredients: Collection<IngredientInput>,
            cuisines: Collection<String>,
            mealType: MealType
    ): Meal {
        validateMealQuantity(ingredients, quantity)

        val createdMeal = dbMealRepository.insert(
                submitterId = submitterId,
                mealName = name,
                cuisines = cuisines,
                ingredients = ingredients,
                quantity = quantity,
                type = mealType
        )

        return dbMealResponseMapper.mapTo(createdMeal)
    }

    fun editMeal(
            submissionId: Int,
            submitterId: Int,
            name: String,
            quantity: Int,
            ingredients: Collection<IngredientInput>,
            cuisines: Collection<String>,
            mealType: MealType
    ): Meal {
        validateMealQuantity(ingredients, quantity)

        val updatedMeal = dbMealRepository.update(
                submissionId = submissionId,
                submitterId = submitterId,
                mealName = name,
                cuisines = cuisines,
                ingredients = ingredients,
                quantity = quantity,
                type = mealType
        )

        return dbMealResponseMapper.mapTo(updatedMeal)
    }

    private fun validateMealQuantity(ingredients: Collection<IngredientInput>, quantity: Int) {
        if (ingredients.sumBy { it.quantity!! } > quantity) {
            throw InvalidMealException("The sum of ingredient quantity must be lower than meal quantity!")
        }
    }
}