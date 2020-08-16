package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food.IngredientResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.model.food.Ingredient
import java.util.stream.Stream

@Service
class IngredientService(
        private val mealDbRepository: MealDbRepository,
        private val ingredientResponseMapper: IngredientResponseMapper
) {
    fun getIngredients(skip: Int?, limit: Int?): Stream<Ingredient> {
        return mealDbRepository
                .getAllIngredients(skip, limit)
                .map(ingredientResponseMapper::mapTo)
    }
}