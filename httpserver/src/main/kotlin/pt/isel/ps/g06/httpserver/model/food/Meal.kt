package pt.isel.ps.g06.httpserver.model.food

import pt.isel.ps.g06.httpserver.dataAccess.model.Cuisine
import pt.isel.ps.g06.httpserver.dataAccess.model.MealComposition
import pt.isel.ps.g06.httpserver.model.NutritionalValues
import pt.isel.ps.g06.httpserver.model.Submitter
import java.net.URI
import java.time.OffsetDateTime
import java.util.stream.Stream

open class Meal(
        identifier: Int,
        name: String,
        imageUri: URI? = null,
        nutritionalValues: NutritionalValues,
        val composedBy: MealComposition,
        val cuisines: Stream<Cuisine>,
        val submitterInfo: Lazy<Submitter?>,
        val creationDate: Lazy<OffsetDateTime?>
) : Food(
        identifier = identifier,
        name = name,
        imageUri = imageUri,
        nutritionalValues = nutritionalValues
) {
    /**
     * Checks if given Meal belongs to a User.
     * This also allows to know that given Meal **is not** a suggested Meal if false, as suggested Meals
     * have no submitter/owner
     */
    fun isUserMeal(): Boolean {
        return submitterInfo.value != null
    }
}