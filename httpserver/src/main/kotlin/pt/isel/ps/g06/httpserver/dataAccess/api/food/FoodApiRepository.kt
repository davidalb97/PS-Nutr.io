package pt.isel.ps.g06.httpserver.dataAccess.api.food

import org.springframework.beans.factory.BeanFactory
import org.springframework.context.annotation.Configuration

@Configuration
class FoodApiRepository(factory: BeanFactory) {

    private val foodApis = mapOf(
            Pair(FoodApiType.Spoonacular, factory.getBean(SpoonacularFoodApi::class.java))
    )

    /**
     * @throws NoSuchElementException when api name is invalid
     */
    fun getFoodApi(typeName: String): IFoodApi =
            getFoodApi(FoodApiType.values().first { it.name == typeName })


    /**
     * @throws NoSuchElementException when api name is invalid
     */
    fun getFoodApi(type: FoodApiType): IFoodApi = foodApis.getValue(type)
}
