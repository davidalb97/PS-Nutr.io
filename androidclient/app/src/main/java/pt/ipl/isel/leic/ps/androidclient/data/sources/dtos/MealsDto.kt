package pt.ipl.isel.leic.ps.androidclient.data.sources.dtos

import pt.ipl.isel.leic.ps.androidclient.data.sources.model.Meal

data class MealsDto(val meals: Array<MealDto>): IUnDto<List<Meal>> {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MealsDto

        if (!meals.contentEquals(other.meals)) return false

        return true
    }

    override fun hashCode(): Int {
        return meals.contentHashCode()
    }

    override fun unDto(): List<Meal> = meals.map(MealDto::unDto)

}

data class MealDto(
        val name: String,
        val apiId: Int,
        val apiTypeStr: String
): IUnDto<Meal> {
    override fun unDto(): Meal = Meal(name, apiId, apiTypeStr)
}