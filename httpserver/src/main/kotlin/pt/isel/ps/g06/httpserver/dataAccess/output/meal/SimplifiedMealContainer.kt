package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import pt.isel.ps.g06.httpserver.model.food.Meal

data class SimplifiedMealContainer(
        val meals: Collection<BaseMealOutput>
)

fun toSimplifiedMealContainer(meals: Sequence<Meal>, userId: Int? = null): SimplifiedMealContainer {
    return SimplifiedMealContainer(
            meals = meals
                    .map { toBaseMealOutput(it, userId) }
                    .toList()
    )
}