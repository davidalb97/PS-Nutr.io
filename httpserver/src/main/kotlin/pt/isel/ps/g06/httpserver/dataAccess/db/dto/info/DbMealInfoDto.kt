package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

class DbMealInfoDto(
        val mealItem: DbMealItemDto,
        val cuisines: Collection<String>,
        val ingredients: Collection<DbMealIngredientInfoDto>,
        val carbs: Int,
        val amount: Int,
        val unit: String
)