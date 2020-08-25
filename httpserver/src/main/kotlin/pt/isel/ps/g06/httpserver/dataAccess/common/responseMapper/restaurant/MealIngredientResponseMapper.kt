package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.common.nutrition.calculateCarbsFromBase
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.*
import pt.isel.ps.g06.httpserver.model.MealComposition
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.MealIngredient
import pt.isel.ps.g06.httpserver.model.NutritionalValues

@Component
class DbMealIngredientResponseMapper(
        private val dbFavoriteRepo: FavoriteDbRepository,
        private val dbMealRepo: MealDbRepository
) : ResponseMapper<DbMealDto, Sequence<MealIngredient>> {
    override fun mapTo(dto: DbMealDto): Sequence<MealIngredient> {
        return dbMealRepo
                .getMealIngredients(dto.submission_id)
                .map {
                    //A Meal Ingredient tuple always requires an existing ingredient tuple
                    val ingredient = dbMealRepo.getById(it.ingredient_submission_id)!!
                    MealIngredient(
                            identifier = ingredient.submission_id,
                            name = ingredient.meal_name,
                            imageUri = null,
                            isFavorite = { userId -> dbFavoriteRepo.getFavorite(ingredient.submission_id, userId) },
                            nutritionalValues = NutritionalValues(
                                    carbs = calculateCarbsFromBase(ingredient.amount, ingredient.carbs, it.quantity).toInt(),
                                    amount = it.quantity,
                                    unit = "gr"
                            )
                    )
                }
    }
}

@Component
class DbMealComponentResponseMapper(
        private val dbMealRepo: MealDbRepository,
        private val dbFavoriteRepo: FavoriteDbRepository,
        private val dbMealIngredientResponseMapper: DbMealIngredientResponseMapper,
        private val dbSubmitterMapper: DbSubmitterResponseMapper,
        private val dbCuisineMapper: DbCuisineResponseMapper,
        private val dbRestaurantMeal: RestaurantMealDbRepository,
        private val dbCuisineRepo: CuisineDbRepository,
        private val dbSubmitterRepo: SubmitterDbRepository,
        private val restaurantMealResponseMapper: DbRestaurantMealInfoResponseMapper
) : ResponseMapper<DbMealDto, Sequence<Meal>> {
    override fun mapTo(dto: DbMealDto): Sequence<Meal> {
        return dbMealRepo
                .getMealComponents(dto.submission_id)
                .map {
                    //A Meal Ingredient tuple always requires an existing meal tuple
                    val mealComponent = dbMealRepo.getById(it.ingredient_submission_id)!!
                    Meal(
                            identifier = dto.submission_id,
                            name = dto.meal_name,
                            isFavorite = { userId -> dbFavoriteRepo.getFavorite(dto.submission_id, userId) },
                            imageUri = null,
                            composedBy = MealComposition(
                                    meals = this.mapTo(mealComponent),
                                    ingredients = dbMealIngredientResponseMapper.mapTo(mealComponent)
                            ),
                            cuisines = dbCuisineRepo.getAllByMealId(dto.submission_id).map(dbCuisineMapper::mapTo),
                            submitterInfo = lazy {
                                dbSubmitterRepo.getSubmitterForSubmission(dto.submission_id)?.let { userInfoDto ->
                                    dbSubmitterMapper.mapTo(userInfoDto)
                                }
                            },
                            creationDate = lazy { dbMealRepo.getCreationDate(dto.submission_id) },
                            restaurantInfoSupplier = { restaurantIdentifier ->
                                restaurantIdentifier.submissionId
                                        ?.let { id -> dbRestaurantMeal.getRestaurantMeal(id, dto.submission_id) }
                                        ?.let(restaurantMealResponseMapper::mapTo)
                            },
                            nutritionalValues = NutritionalValues(
                                    carbs = calculateCarbsFromBase(mealComponent.amount, mealComponent.carbs, it.quantity).toInt(),
                                    amount = it.quantity,
                                    unit = "gr"     //TODO From an enum
                            )
                    )
                }

    }
}


//TODO Replace this with first mapper
@Component
class DbIngredientResponseMapper(
        private val dbFavoriteRepo: FavoriteDbRepository
) : ResponseMapper<DbMealDto, MealIngredient> {
    override fun mapTo(dto: DbMealDto): MealIngredient {
        return MealIngredient(
                identifier = dto.submission_id,
                name = dto.meal_name,
                isFavorite = { userId -> dbFavoriteRepo.getFavorite(dto.submission_id, userId) },
                imageUri = null,
                nutritionalValues = NutritionalValues(dto.carbs, dto.amount, dto.unit)
        )
    }
}