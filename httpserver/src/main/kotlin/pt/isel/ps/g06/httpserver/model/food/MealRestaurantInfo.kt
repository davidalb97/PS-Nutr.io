package pt.isel.ps.g06.httpserver.model.food

import pt.isel.ps.g06.httpserver.model.Portion
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.model.submission.Favorable
import pt.isel.ps.g06.httpserver.model.submission.IFavorable
import pt.isel.ps.g06.httpserver.model.submission.IVotable
import pt.isel.ps.g06.httpserver.model.submission.Votable
import java.util.stream.Stream

data class MealRestaurantInfo(
        val restaurantMealIdentifier: Int,
        val portions: Stream<Portion>,
        val userPortion: (Submitter) -> Portion?,
        override val votable: Votable,
        override val favorable: Favorable
) : IVotable, IFavorable