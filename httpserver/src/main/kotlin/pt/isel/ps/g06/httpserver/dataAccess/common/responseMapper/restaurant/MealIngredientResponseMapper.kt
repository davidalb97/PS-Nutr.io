package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.FavoriteDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.model.MealIngredient

@Component
class DbMealIngredientResponseMapper(
        val dbNutritionalMapper: DbNutritionalValuesResponseMapper,
        val dbFavoriteRepo: FavoriteDbRepository,
        val dbMealRepo: MealDbRepository
) : ResponseMapper<DbMealDto, Sequence<MealIngredient>> {

    override fun mapTo(dto: DbMealDto): Sequence<MealIngredient> {
        val collection = lazy {
            val mealIngredientDtos = dbMealRepo.getMealIngredients(dto.submission_id)
            val ingredientDtos = dbMealRepo.getIngredients(dto.submission_id)
            mealIngredientDtos.zip(ingredientDtos) { mealIngredient, ingredient ->
                MealIngredient(
                        submissionId = mealIngredient.ingredient_submission_id,
                        name = ingredient.ingredient_name,
                        nutritionalValues = dbNutritionalMapper.mapTo(mealIngredient),
                        image = null,
                        isFavorite = { userId -> dbFavoriteRepo.getFavorite(ingredient.submission_id, userId) }
                )
            }
        }
        return Sequence { collection.value.iterator() }
    }
}