package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.model.modular.*

class MealRestaurantInfo(
        identifier: Int?,
        override val isFavorable: UserPredicate,
        override val isFavorite: UserPredicate,
        override val isVotable: UserPredicate,
        override val userVote: UserVote,
        override val votes: Lazy<Votes>,
        override val isReportable: UserPredicate,
        val restaurantIdentifier: Int,
        val mealIdentifier: Int,
        val portions: Sequence<Portion>,
        val userPortion: UserPortion,
        val isVerified: Boolean
) : BaseSubmission<Int?>(
        identifier = identifier
), IFavorable, IVotable, IReportable