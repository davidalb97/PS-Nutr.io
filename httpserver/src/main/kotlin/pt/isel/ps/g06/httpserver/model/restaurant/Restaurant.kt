package pt.isel.ps.g06.httpserver.model.restaurant

import pt.isel.ps.g06.httpserver.model.Cuisine
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.model.Votes
import pt.isel.ps.g06.httpserver.model.modular.*
import java.net.URI
import java.time.OffsetDateTime

class Restaurant(
        identifier: Lazy<RestaurantIdentifier>,
        name: String,
        image: URI?,
        override val isFavorite: UserPredicate,
        override val isFavorable: UserPredicate,
        override val userVote: UserVote,
        override val votes: Lazy<Votes>,
        override val isVotable: UserPredicate,
        override val isReportable: UserPredicate,
        override val cuisines: Sequence<Cuisine>,
        val ownerId: Int?,
        val latitude: Float,
        val longitude: Float,
        val submitterInfo: Lazy<Submitter>,
        val creationDate: Lazy<OffsetDateTime?>,
        val meals: Sequence<Meal>,
        val suggestedMeals: Sequence<Meal>
) : BasePublicSubmission<Lazy<RestaurantIdentifier>>(
        identifier = identifier,
        image = image,
        name = name
), IVotable, IReportable, ICuisines, IFavorable {
    /**
     * Checks if given restaurant is present in the database or not.
     * *This operation is stateful and initializes [identifier] value.*
     */
    fun isPresentInDatabase(): Boolean {
        return identifier.value.isPresentInDatabase()
    }

    /**
     * Checks if given restaurant was created by a user or not.
     * *This operation is stateful and initializes [submitterInfo] value.*
     */
    fun isUserRestaurant(): Boolean {
        return submitterInfo.value.isUser
    }
}