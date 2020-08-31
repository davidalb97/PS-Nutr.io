package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.RestaurantMeal

data class PortionsOutput(
        val userPortion: Int?,
        val portions: Collection<Int>
)

fun toPortionsOutput(restaurantMeal: RestaurantMeal, userId: Int? = null): PortionsOutput {
    return PortionsOutput(
            userPortion = restaurantMeal.info?.userPortion?.invoke(userId)?.amount,
            portions = restaurantMeal.info?.portions
                    ?.map { portion -> portion.amount }
                    ?.toList()
                    ?: emptyList()
    )
}