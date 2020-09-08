package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.db.MealType
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.meal.MealInput
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.DbIngredientModelMapper
import pt.isel.ps.g06.httpserver.model.MealIngredient

@Service
class IngredientService(
        private val mealDbRepository: MealDbRepository,
        private val dbIngredientModelMapper: DbIngredientModelMapper
) {

    fun getIngredients(skip: Int?, count: Int?): Sequence<MealIngredient> {
        return mealDbRepository
                .getAllIngredients(skip, count)
                .map(dbIngredientModelMapper::mapTo)
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
                ).let(dbIngredientModelMapper::mapTo)
    }
}