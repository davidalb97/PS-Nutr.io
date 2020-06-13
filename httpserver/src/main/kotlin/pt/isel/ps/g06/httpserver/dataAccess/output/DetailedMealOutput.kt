package pt.isel.ps.g06.httpserver.dataAccess.output

import java.net.URI
import java.time.OffsetDateTime

data class DetailedMealOutput(
        val id: Int,
        val name: String,
        val imageUri: URI?,
        val isFavorite: Boolean,
        val creationDate: OffsetDateTime,
        val composedBy: MealComposition?,
        val nutritionalInfo: NutritionalInfoOutput,
        val createdBy: SimplifiedUserOutput?
)
