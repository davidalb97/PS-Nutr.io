package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.*
import pt.isel.ps.g06.httpserver.model.Meal

@Component
class DbMealResponseMapper(
        private val ingredientMapper: DbMealIngredientResponseMapper,
        private val nutritionalMapper: DbNutritionalValuesResponseMapper,
        private val dbCreatorMapper: DbCreatorResponseMapper,
        private val dbCuisineMapper: DbCuisineResponseMapper,
        private val dbMealRepo: MealDbRepository,
        private val dbRestaurantMeal: RestaurantMealDbRepository,
        private val dbCuisineRepo: CuisineDbRepository,
        private val dbFavoriteRepo: FavoriteDbRepository,
        private val dbSubmitterRepo: SubmitterDbRepository,
        private val restaurantMealResponseMapper: DbRestaurantMealResponseMapper
) : ResponseMapper<DbMealDto, Meal> {
    override fun mapTo(dto: DbMealDto): Meal {
        return Meal(
                identifier = dto.submission_id,
                name = dto.meal_name,
                isFavorite = { userId -> dbFavoriteRepo.getFavorite(dto.submission_id, userId) },
                nutritionalValues = nutritionalMapper.mapTo(dto),
                //TODO replace image with the one from db!
                imageUri = null,
                ingredients = ingredientMapper.mapTo(dto),
                cuisines = dbCuisineRepo.getAllByMealId(dto.submission_id).map(dbCuisineMapper::mapTo),
                creatorInfo = lazy {
                    dbSubmitterRepo.getBySubmissionId(dto.submission_id)?.let { userInfoDto ->
                        dbCreatorMapper.mapTo(userInfoDto)
                    }
                },
                creationDate = lazy { dbMealRepo.getCreationDate(dto.submission_id) },
                restaurantInfo = { restaurantIdentifier ->
                    restaurantIdentifier.submissionId
                            ?.let { dbRestaurantMeal.getRestaurantMeal(it, dto.submission_id) }
                            ?.let(restaurantMealResponseMapper::mapTo)
                }
        )
    }
}