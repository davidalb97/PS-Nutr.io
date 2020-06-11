package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto

open class DbRestaurantMealItemDto(
        val mealItem: DbMealItemDto,
        val public: DbPublicDto,
        val restaurantMeal: DbRestaurantMealDto
)