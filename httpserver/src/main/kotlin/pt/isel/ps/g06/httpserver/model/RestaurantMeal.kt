package pt.isel.ps.g06.httpserver.model

data class RestaurantMeal(
        val meal: Meal,
        val votes: Votes,
        val userVote: (Int) -> Boolean?,
        val portions: Sequence<Portion>,
        val userPortion: (Int) -> Portion?
)