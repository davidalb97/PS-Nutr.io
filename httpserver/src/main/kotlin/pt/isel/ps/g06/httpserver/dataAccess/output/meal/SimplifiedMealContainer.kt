package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import pt.isel.ps.g06.httpserver.model.Meal
import java.util.stream.Stream
import kotlin.streams.toList


data class SimplifiedMealContainer(
        val meals: Collection<BaseMealOutput>
)

fun toSimplifiedMealContainer(meals: Stream<Meal>, userId: Int? = null): SimplifiedMealContainer {
    return SimplifiedMealContainer(
            meals = meals
                    .map { toBaseMealOutput(it, userId) }
                    .toList()
    )
}