package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.MealDto
import pt.isel.ps.g06.httpserver.dataAccess.api.food.mapper.FoodApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food.MealResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.IngredientInput
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.util.log

@Service
class MealService(
        private val foodApiMapper: FoodApiMapper,
        private val mealResponseMapper: MealResponseMapper,
        private val mealDbRepository: MealDbRepository
) {

    fun searchMeals(name: String, cuisines: Collection<String>?, apiType: String?): Collection<Meal> {
        val type = FoodApiType.getOrDefault(apiType)
        val foodApi = foodApiMapper.getFoodApi(type)

        return foodApi
                .searchMeals(name = name, cuisines = cuisines)
                .exceptionally { exception ->
                    log(exception)
                    return@exceptionally emptyList()
                }
                .thenApply { mealDtos ->
                    mealDtos.map(mealResponseMapper::mapTo)
                }
                .get()
    }

    fun getMeal(mealId: String, apiType: String?): Meal? {
        val type = FoodApiType.getOrDefault(apiType)
        val foodApi = foodApiMapper.getFoodApi(type)

        //If 'id' cannot be converted to Integer, then don't search in database.
        //This is a specification that happens because all meals are submissions, and as such,
        //their unique identifier is always an auto-incremented integer
        val meal = mealId
                .toIntOrNull()
                ?.let { mealDbRepository.getById(it) }
                ?: foodApi.getMealInfo(mealId).get()

        return meal?.let(mealResponseMapper::mapTo)
    }

    fun createMeal(name: String, ingredients: Collection<IngredientInput>, cuisines: Collection<String>): Meal {
        TODO("Not yet implemented")
    }

    private fun handleSearchResult(meals: Collection<MealDto>?, exception: Throwable?): Collection<Meal> {
        if (exception != null) {
            //TODO proper handling here, but what?
            return emptyList()
        }

        return meals?.map(mealResponseMapper::mapTo) ?: emptyList()
    }
}