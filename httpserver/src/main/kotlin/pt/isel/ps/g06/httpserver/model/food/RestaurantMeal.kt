package pt.isel.ps.g06.httpserver.model.food

import pt.isel.ps.g06.httpserver.dataAccess.model.Cuisine
import pt.isel.ps.g06.httpserver.dataAccess.model.MealComposition
import pt.isel.ps.g06.httpserver.model.NutritionalValues
import pt.isel.ps.g06.httpserver.model.Portion
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.model.submission.*
import java.net.URI
import java.time.OffsetDateTime
import java.util.stream.Stream

//TODO Field "added on date"
class RestaurantMeal(
        mealIdentifier: Int,
        val restaurantMealIdentifier: Int,
        name: String,
        imageUri: URI? = null,
        nutritionalValues: NutritionalValues,
        composedBy: MealComposition,
        cuisines: Stream<Cuisine>,
        submitterInfo: Lazy<Submitter?>,
        creationDate: Lazy<OffsetDateTime?>,
        val portions: Stream<Portion>,
        val userPortion: (Submitter) -> Portion?,
        override val votable: Votable,
        override val favorable: Favorable
) : IVotable, IFavorable, IReportable, Meal(
        mealIdentifier,
        name,
        imageUri,
        nutritionalValues,
        composedBy,
        cuisines,
        submitterInfo,
        creationDate
)