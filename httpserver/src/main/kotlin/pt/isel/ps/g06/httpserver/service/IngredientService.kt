package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.db.MealType
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.DbIngredientModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.meal.SuggestedMealInput
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

    fun insertSuggestedIngredient(submitterId: Int, suggestedMealInput: SuggestedMealInput): MealIngredient {
        return mealDbRepository
                .insertSuggestedMeal(
                        submitterId = submitterId,
                        mealName = suggestedMealInput.name!!,
                        quantity = suggestedMealInput.quantity!!,
                        cuisines = suggestedMealInput.cuisines!!,
                        carbs = suggestedMealInput.carbs!!,
                        type = MealType.SUGGESTED_INGREDIENT
                ).let(dbIngredientModelMapper::mapTo)
    }
}