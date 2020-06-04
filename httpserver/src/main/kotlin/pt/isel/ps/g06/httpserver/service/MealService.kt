package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.food.mapper.FoodApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food.MealResponseMapper
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.util.log

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
                .exceptionally { exception ->
                    log(exception)
                    return@exceptionally emptyList()
                }
                .thenApply { mealDtos ->
                    mealDtos.map(mealResponseMapper::mapTo)
                }
                .get()
    }
}