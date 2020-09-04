package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.RestaurantMealOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.MealItem


class OutputRestaurantMealMapper {

    fun mapToOutputModel(mealItem: MealItem) = RestaurantMealOutput(
        mealId = mealItem.submissionId!!
    )
}