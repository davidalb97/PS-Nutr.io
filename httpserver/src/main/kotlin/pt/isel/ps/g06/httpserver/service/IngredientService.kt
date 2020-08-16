package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food.DbIngredientResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.model.food.Ingredient

@Service
class IngredientService(
        private val mealDbRepository: MealDbRepository,
        private val ingredientResponseMapper: DbIngredientResponseMapper
) {
    fun getIngredients(skip: Int?, limit: Int?): Sequence<Ingredient> {
        return mealDbRepository
                .getAllIngredients(skip, limit)
                .map(ingredientResponseMapper::mapTo)
    }
}