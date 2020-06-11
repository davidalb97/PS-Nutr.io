package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbMealInfoDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbMealItemDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbRestaurantMealInfoDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.info.DbRestaurantMealItemDto
import pt.isel.ps.g06.httpserver.model.*

//DbRestaurantMealItemDto -> RestaurantMealItem
@Component
class RestaurantMealItemResponseMapper : ResponseMapper<DbRestaurantMealItemDto, RestaurantMealItem> {
    override fun mapTo(dto: DbRestaurantMealItemDto): RestaurantMealItem {
        return RestaurantMealItem(
                restaurantMealId = dto.restaurantMeal.submission_id,
                mealId = dto.mealItem.meal.submission_id,
                name = dto.mealItem.meal.meal_name,
                votes = Votes(
                        positive = dto.public.votes.positive_count,
                        negative = dto.public.votes.negative_count
                ),
                userVote = dto.public.userVote,
                isFavorite = dto.mealItem.isFavorite,
                image = dto.mealItem.image
        )
    }
}

//DbMealItemDto -> MealItem
@Component
class MealItemResponseMapper : ResponseMapper<DbMealItemDto, MealItem> {
    override fun mapTo(dto: DbMealItemDto): MealItem {
        return MealItem(
                identifier = dto.meal.submission_id,
                name = dto.meal.meal_name,
                isFavorite = dto.isFavorite,
                image = dto.image
        )
    }
}