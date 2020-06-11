package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbPortionDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbVotesDto


open class DbRestaurantMealInfoDto(
        val mealInfo: DbMealInfoDto,
        val public: DbPublicDto,
        val restaurantMealDto: DbRestaurantMealDto,
        val portions: Collection<DbPortionDto>
)