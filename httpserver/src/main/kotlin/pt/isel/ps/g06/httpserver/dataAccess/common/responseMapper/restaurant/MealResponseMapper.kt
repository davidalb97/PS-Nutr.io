package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.*
import pt.isel.ps.g06.httpserver.model.MealComposition
import pt.isel.ps.g06.httpserver.model.Meal

@Component
class DbMealResponseMapper(
        private val ingredientMapper: DbMealIngredientResponseMapper,
        private val mealComponentMapper: DbMealComponentResponseMapper,
        private val nutritionalMapper: DbNutritionalValuesResponseMapper,
        private val dbSubmitterMapper: DbSubmitterResponseMapper,
        private val dbCuisineMapper: DbCuisineResponseMapper,
        private val dbMealRepo: MealDbRepository,
        private val dbRestaurantMeal: RestaurantMealDbRepository,
        private val dbCuisineRepo: CuisineDbRepository,
        private val dbFavoriteRepo: FavoriteDbRepository,
        private val dbSubmitterRepo: SubmitterDbRepository,
        private val restaurantMealResponseMapper: DbRestaurantMealInfoResponseMapper
) : ResponseMapper<DbMealDto, Meal> {
    override fun mapTo(dto: DbMealDto): Meal {
        return Meal(
                identifier = dto.submission_id,
                name = dto.meal_name,
                isFavorite = { userId -> dbFavoriteRepo.getFavorite(dto.submission_id, userId) },
                nutritionalValues = nutritionalMapper.mapTo(dto),
                imageUri = null,
                composedBy = MealComposition(
                        meals = mealComponentMapper.mapTo(dto),
                        ingredients = ingredientMapper.mapTo(dto)
                ),
                cuisines = dbCuisineRepo.getAllByMealId(dto.submission_id).map(dbCuisineMapper::mapTo),
                submitterInfo = lazy {
                    dbSubmitterRepo
                            .getSubmitterForSubmission(dto.submission_id)
                            ?.let { submitter -> dbSubmitterMapper.mapTo(submitter) }
                },
                creationDate = lazy { dbMealRepo.getCreationDate(dto.submission_id) },
                restaurantInfoSupplier = { restaurantIdentifier ->
                    restaurantIdentifier.submissionId
                            ?.let { dbRestaurantMeal.getRestaurantMeal(it, dto.submission_id) }
                            ?.let(restaurantMealResponseMapper::mapTo)
                }
        )
    }
}