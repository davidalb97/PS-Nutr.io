package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.DbIngredientResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.model.MealIngredient

@Service
class IngredientService(
        private val mealDbRepository: MealDbRepository,
        private val ingredientResponseMapper: DbIngredientResponseMapper
) {
    fun getIngredients(skip: Int?, limit: Int?): Sequence<MealIngredient> {
        return mealDbRepository
                .getAllIngredients(skip, limit)
                .map(ingredientResponseMapper::mapTo)
    }
}