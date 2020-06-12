package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import java.time.OffsetDateTime

open class DbMealItemDto(
        val meal: DbMealDto,
        val image: String?,
        val isFavorite: Boolean?,
        val creationDate: OffsetDateTime
)