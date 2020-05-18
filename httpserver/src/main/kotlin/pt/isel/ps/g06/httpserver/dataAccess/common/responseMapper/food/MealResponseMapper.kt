package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.MealDto
import pt.isel.ps.g06.httpserver.dataAccess.api.food.dto.spoonacular.SpoonacularMealDto
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.util.log

@Component
class MealResponseMapper(
        private val spoonacularResponseMapper: SpoonacularResponseMapper
) : ResponseMapper<MealDto, Meal> {
    override fun mapTo(dto: MealDto): Meal {
        return when (dto) {
            is SpoonacularMealDto -> spoonacularResponseMapper.mapTo(dto)
            else -> {
                log("ERROR: Unregistered mapper for MealDto of type '${dto.javaClass.typeName}'!")
                //TODO Should a handler listen to this exception?
                throw NoSuchMethodException("There is no mapper registered for given dto type!")
            }
        }
    }
}


@Component
class SpoonacularResponseMapper : ResponseMapper<SpoonacularMealDto, Meal> {
    override fun mapTo(dto: SpoonacularMealDto): Meal {
        return Meal(dto.id, dto.name)
    }
}