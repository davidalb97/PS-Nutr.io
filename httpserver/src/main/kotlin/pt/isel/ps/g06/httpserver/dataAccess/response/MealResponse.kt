package pt.isel.ps.g06.httpserver.dataAccess.response

data class MealResponse(val mealId: Int, val mealName: String)

data class MealsResponse(val meals: Array<MealResponse>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MealsResponse

        if (!meals.contentEquals(other.meals)) return false

        return true
    }

    override fun hashCode(): Int {
        return meals.contentHashCode()
    }
}