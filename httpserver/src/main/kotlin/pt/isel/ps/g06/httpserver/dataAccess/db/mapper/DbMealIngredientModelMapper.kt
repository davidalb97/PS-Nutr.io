package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.common.nutrition.calculateCarbsFromBase
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.FavoriteDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.model.MealIngredient
import pt.isel.ps.g06.httpserver.model.NutritionalValues
import pt.isel.ps.g06.httpserver.model.modular.toUserPredicate

@Component
class DbMealIngredientModelMapper(
        private val dbFavoriteRepo: FavoriteDbRepository,
        private val dbMealRepo: MealDbRepository
) : ModelMapper<DbMealDto, Sequence<MealIngredient>> {
    override fun mapTo(dto: DbMealDto): Sequence<MealIngredient> {
        return dbMealRepo
                .getMealIngredients(dto.submission_id)
                .map {
                    //A Meal Ingredient tuple always requires an existing ingredient tuple
                    val ingredient = dbMealRepo.getById(it.ingredient_submission_id)!!
                    MealIngredient(
                            identifier = ingredient.submission_id,
                            name = ingredient.meal_name,
                            image = null,
                            isFavorite = toUserPredicate({ false }) { userId ->
                                dbFavoriteRepo.getFavorite(ingredient.submission_id, userId)
                            },
                            //An ingredient is always favorable
                            isFavorable = { true },
                            nutritionalValues = NutritionalValues(
                                    carbs = calculateCarbsFromBase(ingredient.amount, ingredient.carbs, it.quantity).toInt(),
                                    amount = it.quantity,
                                    unit = "gr"
                            )
                    )
                }
    }
}