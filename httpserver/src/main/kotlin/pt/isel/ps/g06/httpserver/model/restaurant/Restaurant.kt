package pt.isel.ps.g06.httpserver.model.restaurant

import pt.isel.ps.g06.httpserver.dataAccess.model.Cuisine
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.model.food.RestaurantMeal
import pt.isel.ps.g06.httpserver.model.submission.Favorable
import pt.isel.ps.g06.httpserver.model.submission.IFavorable
import pt.isel.ps.g06.httpserver.model.submission.IVotable
import pt.isel.ps.g06.httpserver.model.submission.Votable
import java.net.URI
import java.time.OffsetDateTime
import java.util.stream.Stream

data class Restaurant(
        //TODO remove lazy?
        val identifier: Lazy<RestaurantIdentifier>,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val image: URI? = null,
        val submitterInfo: Lazy<Submitter>,
        val creationDate: Lazy<OffsetDateTime?>,
        val meals: Stream<RestaurantMeal>,
        val suggestedMeals: Stream<RestaurantMeal>,
        val cuisines: Stream<Cuisine>,
        override val votable: Votable,
        override val favorable: Favorable
) : IVotable, IFavorable {
    /**
     * Checks if given restaurant is present in the database or not.
     * *This operation is stateful and initializes [identifier] value.*
     */
    fun isPresentInDatabase(): Boolean {
        return identifier.value.isPresentInDatabase()
    }

    /**
     * Checks if given restaurant was created by a user or not.
     */
    fun wasCreatedByUser(): Boolean {
        return submitterInfo.isUser
    }
}