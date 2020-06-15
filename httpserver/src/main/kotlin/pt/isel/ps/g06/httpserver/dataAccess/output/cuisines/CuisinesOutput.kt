package pt.isel.ps.g06.httpserver.dataAccess.output.cuisines

import pt.isel.ps.g06.httpserver.model.Cuisine

data class CuisinesOutput(val cuisines: Collection<String>)

fun toSimplifiedCuisinesOutput(cuisines: Collection<Cuisine>): CuisinesOutput {
    return CuisinesOutput(cuisines = cuisines.map { it.name })
}