package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.MealType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.*
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.MealComposition
import pt.isel.ps.g06.httpserver.model.modular.toUserPredicate
import pt.isel.ps.g06.httpserver.util.memoized

@Component
class DbMealModelMapper(
        private val ingredientMapper: DbMealIngredientModelMapper,
        private val mealComponentMapper: DbMealComponentModelMapper,
        private val nutritionalMapper: DbNutritionalValuesModelMapper,
        private val dbSubmitterMapper: DbSubmitterModelMapper,
        private val dbCuisineMapper: DbCuisineModelMapper,
        private val dbRestaurantMeal: RestaurantMealDbRepository,
        private val dbCuisineRepo: CuisineDbRepository,
        private val dbFavoriteRepo: FavoriteDbRepository,
        private val dbSubmitterRepo: SubmitterDbRepository,
        private val restaurantMealModelMapper: DbRestaurantMealInfoModelMapper,
        private val dbSubmissionRepository: SubmissionDbRepository
) : ModelMapper<DbMealDto, Meal> {
    override fun mapTo(dto: DbMealDto): Meal {
        return Meal(
                identifier = dto.submission_id,
                name = dto.meal_name,
                isFavorite = toUserPredicate({ false }) { userId ->
                    dbFavoriteRepo.getFavorite(dto.submission_id, userId)
                },
                nutritionalInfo = nutritionalMapper.mapTo(dto),
                image = null,
                composedBy = MealComposition(
                        meals = mealComponentMapper.mapTo(dto),
                        ingredients = ingredientMapper.mapTo(dto)
                ),
                cuisines = dbCuisineRepo.getAllByMealId(dto.submission_id)
                        .map(dbCuisineMapper::mapTo)
                        .memoized(),
                submitterInfo = lazy {
                    dbSubmitterRepo
                            .getSubmitterForSubmission(dto.submission_id)
                            ?.let(dbSubmitterMapper::mapTo)
                },
                creationDate = lazy { dbSubmissionRepository.getCreationDate(dto.submission_id) },
                type = MealType.fromValue(dto.meal_type),
                restaurantInfoSupplier = { restaurantIdentifier ->
                    restaurantIdentifier.submissionId
                            ?.let { dbRestaurantMeal.getRestaurantMeal(it, dto.submission_id) }
                            ?.let(restaurantMealModelMapper::mapTo)
                }
        )
    }
}