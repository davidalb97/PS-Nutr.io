package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

class DbMealIngredientInfoDto(
        val ingredient: DbIngredientInfoDto,
        val carbs: Int,
        val amount: Int
)