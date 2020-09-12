package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.CustomMealOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomMeal


class OutputCustomMealMapper(
    private val cuisineMapper: OutputCuisineMapper,
    private val ingredientMapper: OutputIngredientMapper
) {

    fun mapToOutputModel(restaurant: CustomMeal) = CustomMealOutput(
        name = restaurant.name,
        quantity = restaurant.amount,
        unit = restaurant.unit.toString(),
        ingredients = ingredientMapper.mapToOutputModelCollection(
            restaurant.mealComponents.plus(
                restaurant.ingredientComponents
            )
        ),
        cuisines = cuisineMapper.mapToOutputModelCollection(restaurant.cuisines),
        imageUri = restaurant.imageUri.toString()
    )
}