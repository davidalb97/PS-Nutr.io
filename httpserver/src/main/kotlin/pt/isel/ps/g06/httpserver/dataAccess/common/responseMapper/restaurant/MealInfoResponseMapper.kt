package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbMealInfoDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbRestaurantMealInfoDto
import pt.isel.ps.g06.httpserver.model.MealInfo
import pt.isel.ps.g06.httpserver.model.RestaurantMealInfo
import pt.isel.ps.g06.httpserver.model.Votes

//DbRestaurantMealInfoDto -> RestaurantMealInfo
@Component
class RestaurantMealInfoResponseMapper(
        private val ingredientMapper: IngredientResponseMapper,
        private val portionMapper: PortionResponseMapper
) : ResponseMapper<DbRestaurantMealInfoDto, RestaurantMealInfo> {
    override fun mapTo(dto: DbRestaurantMealInfoDto): RestaurantMealInfo {
        return RestaurantMealInfo(
                restaurantMealId = dto.restaurantMealDto.submission_id,
                mealId = dto.mealInfo.mealItem.meal.submission_id,
                name = dto.mealInfo.mealItem.meal.meal_name,
                votes = Votes(
                        positive = dto.public.votes.positive_count,
                        negative = dto.public.votes.negative_count
                ),
                userVote = dto.public.userVote,
                isFavorite = dto.mealInfo.mealItem.isFavorite,
                image = dto.mealInfo.mealItem.image,
                amount = dto.mealInfo.amount,
                carbs = dto.mealInfo.carbs,
                unit = dto.mealInfo.unit,
                cuisines = dto.mealInfo.cuisines,
                ingredients = dto.mealInfo.ingredients.map(ingredientMapper::mapTo),
                portions = dto.portions.map(portionMapper::mapTo)
        )
    }
}

//DbMealInfoDto -> MealInfo
@Component
class MealInfoResponseMapper(
        private val ingredientMapper: IngredientResponseMapper
) : ResponseMapper<DbMealInfoDto, MealInfo> {
    override fun mapTo(dto: DbMealInfoDto): MealInfo {
        return MealInfo(
                identifier = dto.mealItem.meal.submission_id,
                name = dto.mealItem.meal.meal_name,
                isFavorite = dto.mealItem.isFavorite,
                image = dto.mealItem.image,
                carbs = dto.carbs,
                amount = dto.amount,
                unit = dto.unit,
                ingredients = dto.ingredients.map(ingredientMapper::mapTo),
                cuisines = dto.cuisines
        )
    }
}