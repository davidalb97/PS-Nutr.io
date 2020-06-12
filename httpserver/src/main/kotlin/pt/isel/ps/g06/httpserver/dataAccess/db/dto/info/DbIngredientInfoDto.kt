package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

class DbIngredientInfoDto(
        val name: String,
        val submissionId: Int,
        val image: String?,
        val isFavorite: Boolean?
)