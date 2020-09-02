package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import pt.isel.ps.g06.httpserver.dataAccess.output.NutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.cuisines.CuisinesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.cuisines.toSimplifiedCuisinesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.FavoritesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.ICuisinesOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.modular.ISubmitterInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toNutritionalInfoOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.user.SimplifiedUserOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.user.toSimplifiedUserOutput
import pt.isel.ps.g06.httpserver.model.Meal
import java.net.URI
import java.time.OffsetDateTime

class DetailedMealOutput(
        identifier: Int,
        name: String,
        image: URI?,
        nutritionalInfo: NutritionalInfoOutput,
        favorites: FavoritesOutput,
        @JsonSerialize(using = ToStringSerializer::class)
        val creationDate: OffsetDateTime?,
        val composedBy: MealCompositionOutput?,
        override val createdBy: SimplifiedUserOutput?,
        override val cuisines: CuisinesOutput
) : BaseMealOutput(
        identifier = identifier,
        name = name,
        image = image,
        nutritionalInfo = nutritionalInfo,
        favorites = favorites
), ISubmitterInfoOutput, ICuisinesOutput

fun toDetailedMealOutput(meal: Meal, userId: Int? = null): DetailedMealOutput {
    return DetailedMealOutput(
            identifier = meal.identifier,
            name = meal.name,
            image = meal.image,
            favorites = FavoritesOutput(
                    isFavorite = meal.isFavorite(userId),
                    isFavorable = meal.isFavorable(userId)
            ),
            creationDate = meal.creationDate.value,
            composedBy = toMealComposition(meal),
            nutritionalInfo = toNutritionalInfoOutput(meal.nutritionalInfo),
            createdBy = meal.submitterInfo.value?.let(::toSimplifiedUserOutput),
            cuisines = toSimplifiedCuisinesOutput(meal.cuisines.toList())
    )
}