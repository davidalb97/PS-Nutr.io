package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info


open class DbMealInfoDto(
        val submission_id: Int,
        val meal_name: String,
        val positive_count: Int,
        val negative_count: Int
)