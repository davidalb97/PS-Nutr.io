package pt.isel.ps.g06.httpserver.dataAccess.api.food.mapper

import org.springframework.context.annotation.Configuration
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApi
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularFoodApi
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType

@Configuration
class FoodApiMapper(private val spoonacularApi: SpoonacularFoodApi) {

    private val foodApis = mapOf(
            Pair(FoodApiType.Spoonacular, spoonacularApi)
    )

    /**
     *  Get a restaurantApi from given [FoodApiType]
     *  Defaults to [SpoonacularFoodApi]
     */
    fun getFoodApi(type: FoodApiType): FoodApi = foodApis.getOrDefault(type, spoonacularApi)


}
