package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.MealDto
import pt.isel.ps.g06.httpserver.dataAccess.api.food.mapper.FoodApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food.MealResponseMapper
import pt.isel.ps.g06.httpserver.model.Meal

@Service
class MealService(
        private val foodApiMapper: FoodApiMapper,
        private val mealResponseMapper: MealResponseMapper
) {

    fun searchMeals(name: String, cuisines: Collection<String>?, apiType: String?): Collection<Meal> {
        val api = FoodApiType.getOrDefault(apiType)
        val foodApi = foodApiMapper.getFoodApi(api)

        return foodApi
                .searchMeals(name = name, cuisines = cuisines)
                .handle(this::handleSearchResult)
                .get()
    }


    private fun handleSearchResult(meals: Collection<MealDto>?, exception: Throwable?): Collection<Meal> {
        if (exception != null) {
            //TODO proper handling here, but what?
            return emptyList()
        }

        return meals?.map(mealResponseMapper::mapTo) ?: emptyList()
    }
}