package pt.isel.ps.g06.httpserver.dataAccess.output

data class MealComposition(
        val ingredients: Collection<DetailedMealOutput>,
        val meals: Collection<DetailedMealOutput>
)