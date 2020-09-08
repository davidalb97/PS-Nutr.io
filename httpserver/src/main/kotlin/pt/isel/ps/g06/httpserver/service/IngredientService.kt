package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.db.MealType
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.meal.MealInput
import pt.isel.ps.g06.httpserver.model.mapper.restaurant.DbIngredientResponseMapper
import pt.isel.ps.g06.httpserver.model.MealIngredient

@Service
class IngredientService(
        private val mealDbRepository: MealDbRepository,
        private val ingredientResponseMapper: DbIngredientResponseMapper
) {

    fun getIngredients(skip: Int?, count: Int?): Sequence<MealIngredient> {
        return mealDbRepository
                .getAllIngredients(skip, count)
                .map(ingredientResponseMapper::mapTo)
    }

    fun insertSuggestedIngredient(submitterId: Int, mealIngredientInput: MealInput): MealIngredient {
        return mealDbRepository
                .insert(
                        submitterId = submitterId,
                        mealName = mealIngredientInput.name!!,
                        quantity = mealIngredientInput.quantity!!,
                        cuisines = mealIngredientInput.cuisines!!,
                        ingredients = mealIngredientInput.ingredients!!,
                        type = MealType.SUGGESTED_INGREDIENT
                ).let(ingredientResponseMapper::mapTo)
    }
}