package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.common.nutrition.calculateCarbsFromBase
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.MealType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.*
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.MealComposition
import pt.isel.ps.g06.httpserver.model.NutritionalValues
import pt.isel.ps.g06.httpserver.model.modular.toUserPredicate

@Component
class DbMealComponentModelMapper(
        private val dbMealRepo: MealDbRepository,
        private val dbFavoriteRepo: FavoriteDbRepository,
        private val dbMealIngredientModelMapper: DbMealIngredientModelMapper,
        private val dbSubmitterMapper: DbSubmitterModelMapper,
        private val dbCuisineMapper: DbCuisineModelMapper,
        private val dbRestaurantMeal: RestaurantMealDbRepository,
        private val dbCuisineRepo: CuisineDbRepository,
        private val dbSubmitterRepo: SubmitterDbRepository,
        private val dbRestaurantMealModelMapper: DbRestaurantMealInfoModelMapper,
        private val submissionDbRepository: SubmissionDbRepository
) : ModelMapper<DbMealDto, Sequence<Meal>> {
    override fun mapTo(dto: DbMealDto): Sequence<Meal> {
        return dbMealRepo
                .getMealComponents(dto.submission_id)
                .map {
                    //A Meal Ingredient tuple always requires an existing meal tuple
                    val mealComponent = dbMealRepo.getById(it.ingredient_submission_id)!!
                    Meal(
                            identifier = dto.submission_id,
                            name = dto.meal_name,
                            isFavorite = toUserPredicate({ false }) { userId ->
                                dbFavoriteRepo.getFavorite(dto.submission_id, userId)
                            },
                            image = null,
                            composedBy = MealComposition(
                                    meals = this.mapTo(mealComponent),
                                    ingredients = dbMealIngredientModelMapper.mapTo(mealComponent)
                            ),
                            cuisines = dbCuisineRepo.getAllByMealId(dto.submission_id).map(dbCuisineMapper::mapTo),
                            submitterInfo = lazy {
                                dbSubmitterRepo.getSubmitterForSubmission(dto.submission_id)?.let { userInfoDto ->
                                    dbSubmitterMapper.mapTo(userInfoDto)
                                }
                            },
                            creationDate = lazy { submissionDbRepository.getCreationDate(dto.submission_id) },
                            type = MealType.fromValue(dto.meal_type),
                            restaurantInfoSupplier = { restaurantIdentifier ->
                                restaurantIdentifier.submissionId
                                        ?.let { id -> dbRestaurantMeal.getRestaurantMeal(id, dto.submission_id) }
                                        ?.let(dbRestaurantMealModelMapper::mapTo)
                            },
                            nutritionalInfo = NutritionalValues(
                                    carbs = calculateCarbsFromBase(mealComponent.amount, mealComponent.carbs, it.quantity).toInt(),
                                    amount = it.quantity,
                                    unit = "gr"     //TODO From an enum
                            )
                    )
                }

    }
}