package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto

open class DbMealItemDto(
        val meal: DbMealDto,
        val image: String?,
        val isFavorite: Boolean
)