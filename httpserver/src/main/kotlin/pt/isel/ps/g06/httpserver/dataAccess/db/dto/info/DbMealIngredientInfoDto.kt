package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbIngredientDto

class DbMealIngredientInfoDto(
        val ingredient: DbIngredientDto,
        val carbs: Int,
        val amount: Int,
        val image: String?
)