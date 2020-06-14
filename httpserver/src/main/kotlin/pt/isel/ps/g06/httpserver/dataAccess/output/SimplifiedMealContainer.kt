package pt.isel.ps.g06.httpserver.dataAccess.output

import pt.isel.ps.g06.httpserver.model.Meal

data class SimplifiedMealContainer(
        val meals: Collection<SimplifiedMealOutput>
)

fun toSimplifiedMealContainer(meals: Sequence<Meal>, userId: Int? = null): SimplifiedMealContainer {
    return SimplifiedMealContainer(
            meals = meals
                    .map { toSimplifiedMealOutput(it, userId) }
                    .toList()
    )
}