package pt.isel.ps.g06.httpserver.model

class RestaurantMealPortions(
        val portions: Collection<Portion>,
        val userPortion: Portion?
)