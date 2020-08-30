package pt.isel.ps.g06.httpserver.model

data class MealRestaurantInfo(
        val restaurantMealIdentifier: Int?,
        val votes: Lazy<Votes>,
        val userVote: (Int) -> VoteState,
        val portions: Sequence<Portion>,
        val userPortion: (Int) -> Portion?
)