package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food

import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.dataAccess.api.food.SpoonacularFoodApi
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.MealDto
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.spoonacular.SpoonacularDetailedMealDto
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.spoonacular.SpoonacularMealDto
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.model.Ingredient
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.MealInfo
import pt.isel.ps.g06.httpserver.util.log
import java.net.URI

private const val DEFAULT_IMG_SIZE = "90x90"
private const val DEFAULT_IMG_TYPE = "jpg"
private const val RECIPE_IMG_URI = "https://spoonacular.com/recipeImages/{ID}-$DEFAULT_IMG_SIZE.$DEFAULT_IMG_TYPE"
private const val CARBOHYDRATES = "Carbohydrates"

@Component
class MealResponseMapper(
        private val spoonacularResponseMapper: SpoonacularResponseMapper
) : ResponseMapper<MealDto, Meal> {
    override fun mapTo(dto: MealDto): Meal {
        return when (dto) {
            is SpoonacularMealDto -> spoonacularResponseMapper.mapTo(dto)
            is SpoonacularDetailedMealDto -> spoonacularResponseMapper.mapDetailedInfo(dto)
            else -> {
                log("ERROR: Unregistered mapper for MealDto of type '${dto.javaClass.typeName}'!")
                //TODO Should a handler listen to this exception?
                throw NoSuchMethodException("There is no mapper registered for given dto type!")
            }
        }
    }
}


@Component
class SpoonacularResponseMapper(private val spoonacularFoodApi: SpoonacularFoodApi) : ResponseMapper<SpoonacularMealDto, Meal> {
    override fun mapTo(dto: SpoonacularMealDto): Meal {
        return Meal(
                identifier = dto.id,
                name = dto.name,
                image = buildImageURI(dto.id),
                info = lazy { fetchMealInfo(dto.id) }
        )
    }

    fun mapDetailedInfo(dto: SpoonacularDetailedMealDto): Meal {
        return Meal(
                identifier = dto.id,
                name = dto.name,
                image = buildImageURI(dto.id),
                info = lazy { buildMealInfo(dto) }
        )
    }

    private fun buildMealInfo(dto: SpoonacularDetailedMealDto): MealInfo {
        return MealInfo(
                carbohydrates = dto
                        .nutrition
                        .nutrients
                        .firstOrNull { it.title.equals(CARBOHYDRATES, true) }
                        ?.amount,
                //TODO Add proper ingredient mapper
                ingredients = dto.extendedIngredients.map { Ingredient(it.name, it.id) }.stream()
        )
    }

    private fun fetchMealInfo(id: String): MealInfo {
        //TODO Map to own exception
        return spoonacularFoodApi.getMealInfo(id).handle { result, exception ->
            if (exception != null) {
                throw NoSuchElementException()
            }

            return@handle result
                    ?.let { mapDetailedInfo(it as SpoonacularDetailedMealDto) }
                    ?.info?.value
                    ?: throw NoSuchElementException()
        }.get()
    }

    private fun buildImageURI(id: String): URI? {
        return UriComponentsBuilder
                .fromHttpUrl(RECIPE_IMG_URI)
                .buildAndExpand(id, DEFAULT_IMG_SIZE, DEFAULT_IMG_TYPE)
                .toUri()
    }
}