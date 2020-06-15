package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.dataAccess.model.Cuisine
import java.net.URI
import java.time.OffsetDateTime

data class Restaurant(
        val identifier: Lazy<RestaurantIdentifier>,
        val name: String,
        val latitude: Float,
        val longitude: Float,
        val image: URI?,
        val isFavorite: (Int) -> Boolean,
        val userVote: (Int) -> VoteState,
        val votes: Votes,
        val submitterInfo: Lazy<Submitter>,
        val creationDate: Lazy<OffsetDateTime?>,
        val meals: Sequence<Meal>,
        val suggestedMeals: Sequence<Meal>,
        val cuisines: Sequence<Cuisine>
) {
    /**
     * Checks if given restaurant is present in the database or not.
     * *This operation initializes [identifier] value.*
     */
    fun isPresentInDatabase(): Boolean {
        return identifier.value.isPresentInDatabase()
    }
}