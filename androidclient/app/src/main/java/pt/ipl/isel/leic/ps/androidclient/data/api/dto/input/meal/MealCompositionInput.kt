package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal

class MealCompositionInput(
    val ingredients: Collection<MealInfoInput>,
    val meals: Collection<MealInfoInput>
)