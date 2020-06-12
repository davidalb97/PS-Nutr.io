package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.info

class MealComposition(
    val ingredients: Collection<DetailedMealInput>,
    val meals: Collection<DetailedMealInput>
)